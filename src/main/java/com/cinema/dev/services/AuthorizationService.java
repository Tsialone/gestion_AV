package com.cinema.dev.services;

import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for handling authorization and permission checks.
 * 
 * Authorization Rules:
 * 1. Department Restrictions:
 *    - Only 'Ventes' (id=1) can: validate proforma, insert DA, create commande, validate commande, deliver
 *    - Only 'Finance' (id=2) can: access valorisation stock menu
 * 
 * 2. Role/Level Restrictions:
 *    - niveau >= 7: can validate (proforma, commande)
 *    - niveau >= 15: no restrictions (director level)
 * 
 * 3. Category Restrictions:
 *    - If user has entries in restriction_categorie, they can only work with those categories
 *    - If no entries, no restriction (can work with all categories)
 * 
 * 4. Fournisseur Restrictions:
 *    - If user has entries in restriction_fournisseur, they can only work with those fournisseurs
 *    - If no entries, no restriction (can work with all fournisseurs)
 */
@Service
public class AuthorizationService {
    
    // Department IDs
    public static final int DEPT_VENTES = 1;
    public static final int DEPT_FINANCE = 2;
    public static final int DEPT_LOGISTIQUE = 3;
    public static final int DEPT_DIRECTION = 4;
    
    // Minimum levels for actions
    public static final int NIVEAU_VALIDATION = 7;      // Minimum level to validate
    public static final int NIVEAU_DIRECTEUR = 15;      // Director level - no restrictions
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private DeptRepository deptRepository;
    
    @Autowired
    private RestrictionCategorieRepository restrictionCategorieRepository;
    
    @Autowired
    private RestrictionFournisseurRepository restrictionFournisseurRepository;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private HistoriqueGeneralRepository historiqueGeneralRepository;
    
    // ========================================
    // UTILITY METHODS ABOUT USER
    // ========================================
    
    public List<Utilisateur> findAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
    
    public List<Dept> findAllDepts() {
        return deptRepository.findAll();
    }

    public Utilisateur getUtilisateur(Integer idUtilisateur) {
        return utilisateurRepository.findById(idUtilisateur)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé: " + idUtilisateur));
    }
    
    public Role getRole(Integer idUtilisateur) {
        Utilisateur user = getUtilisateur(idUtilisateur);
        return roleRepository.findById(user.getIdRole())
            .orElseThrow(() -> new IllegalArgumentException("Role non trouvé: " + user.getIdRole()));
    }
    
    public Dept getDept(Integer idUtilisateur) {
        Utilisateur user = getUtilisateur(idUtilisateur);
        return deptRepository.findById(user.getIdDept())
            .orElseThrow(() -> new IllegalArgumentException("Département non trouvé: " + user.getIdDept()));
    }

    public int getNiveau(Integer idUtilisateur) {
        return getRole(idUtilisateur).getNiveau();
    }
    
    public boolean isDirecteurLevel(Integer idUtilisateur) {
        return getNiveau(idUtilisateur) >= NIVEAU_DIRECTEUR;
    }
    
    // ========================================
    // DEPARTMENT CHECKS
    // ========================================
    
    /**
     * Check if user is in Ventes department
     */
    public boolean isInVentes(Integer idUtilisateur) {
        if (isDirecteurLevel(idUtilisateur)) return true;
        Utilisateur user = getUtilisateur(idUtilisateur);
        return user.getIdDept() == DEPT_VENTES;
    }
    
    /**
     * Check if user is in Finance department
     */
    public boolean isInFinance(Integer idUtilisateur) {
        if (isDirecteurLevel(idUtilisateur)) return true;
        Utilisateur user = getUtilisateur(idUtilisateur);
        return user.getIdDept() == DEPT_FINANCE;
    }

    /**
     * Check if user is in Direction department
     */
    public boolean isInDirection(Integer idUtilisateur) {
        Utilisateur user = getUtilisateur(idUtilisateur);
        return user.getIdDept() == DEPT_DIRECTION;
    }

    /**
     * Check if user is in Ventes or Direction department (for Achats/Ventes module access)
     */
    public boolean isInVentesOrDirection(Integer idUtilisateur) {
        if (isDirecteurLevel(idUtilisateur)) return true;
        Utilisateur user = getUtilisateur(idUtilisateur);
        return user.getIdDept() == DEPT_VENTES || user.getIdDept() == DEPT_DIRECTION;
    }

    /**
     * Check if user is in a specific department by name
     */
    public boolean isInDepartement(Integer idUtilisateur, String deptName) {
        if (isDirecteurLevel(idUtilisateur)) return true;
        Dept dept = getDept(idUtilisateur);
        return dept.getNom().equalsIgnoreCase(deptName);
    }
    
    
    /**
     * Require user to be in Ventes department
     */
    public void requireVentesDept(Integer idUtilisateur, String action) {
        if (!isInVentes(idUtilisateur)) {
            Dept dept = getDept(idUtilisateur);
            throw new SecurityException(
                "Action non autorisée: " + action + ". " +
                "Seul le département 'Ventes' peut effectuer cette action. " +
                "Votre département: " + dept.getNom()
            );
        }
    }
    
    /**
     * Require user to be in Finance department
     */
    public void requireFinanceDept(Integer idUtilisateur, String action) {
        if (!isInFinance(idUtilisateur)) {
            Dept dept = getDept(idUtilisateur);
            throw new SecurityException(
                "Action non autorisée: " + action + ". " +
                "Seul le département 'Finance' peut effectuer cette action. " +
                "Votre département: " + dept.getNom()
            );
        }
    }

    /**
     * Require user to be in a specific department by name
     */
    public void requireDepartement(Integer idUtilisateur, String deptName, String action) {
        if (!isInDepartement(idUtilisateur, deptName)) {
            Dept dept = getDept(idUtilisateur);
            throw new SecurityException(
                "Action non autorisée: " + action + ". " +
                "Seul le département '" + deptName + "' peut effectuer cette action. " +
                "Votre département: " + dept.getNom()
            );
        }
    }
    
    // ========================================
    // LEVEL/ROLE CHECKS
    // ========================================
    
    /**
     * Check if user can validate (niveau >= 7)
     */
    public boolean canValidate(Integer idUtilisateur) {
        return getNiveau(idUtilisateur) >= NIVEAU_VALIDATION;
    }
    
    /**
     * Require user to have validation rights (niveau >= 7)
     */
    public void requireValidationRights(Integer idUtilisateur, String action) {
        if (!canValidate(idUtilisateur)) {
            Role role = getRole(idUtilisateur);
            throw new SecurityException(
                "Action non autorisée: " + action + ". " +
                "Niveau minimum requis: " + NIVEAU_VALIDATION + ". " +
                "Votre rôle: " + role.getNom() + " (niveau " + role.getNiveau() + ")"
            );
        }
    }
    
    /**
     * Combined check: must be in Ventes AND have validation rights
     */
    public void requireVentesAndValidation(Integer idUtilisateur, String action) {
        requireVentesDept(idUtilisateur, action);
        requireValidationRights(idUtilisateur, action);
    }
    
    // ========================================
    // CATEGORY RESTRICTIONS
    // ========================================
    
    /**
     * Check if user has category restrictions
     */
    public boolean hasCategoryRestrictions(Integer idUtilisateur) {
        if (isDirecteurLevel(idUtilisateur)) return false;
        return restrictionCategorieRepository.hasRestrictions(idUtilisateur);
    }
    
    /**
     * Get list of allowed category IDs for user (empty = all allowed)
     */
    public List<Integer> getAllowedCategories(Integer idUtilisateur) {
        if (isDirecteurLevel(idUtilisateur)) return List.of();
        return restrictionCategorieRepository.findAllowedCategoriesByUtilisateur(idUtilisateur);
    }
    
    /**
     * Check if user can work with a specific category
     */
    public boolean canAccessCategory(Integer idUtilisateur, Integer idCategorie) {
        if (isDirecteurLevel(idUtilisateur)) return true;
        if (!hasCategoryRestrictions(idUtilisateur)) return true;
        return restrictionCategorieRepository.isAllowedForCategory(idUtilisateur, idCategorie);
    }
    
    /**
     * Check if user can work with an article (checks article's category)
     */
    public boolean canAccessArticle(Integer idUtilisateur, Integer idArticle) {
        if (isDirecteurLevel(idUtilisateur)) return true;
        if (!hasCategoryRestrictions(idUtilisateur)) return true;
        
        Article article = articleRepository.findById(idArticle)
            .orElseThrow(() -> new IllegalArgumentException("Article non trouvé: " + idArticle));
        
        return canAccessCategory(idUtilisateur, article.getIdCategorie());
    }
    
    /**
     * Require user to have access to a category
     */
    public void requireCategoryAccess(Integer idUtilisateur, Integer idCategorie, String action) {
        if (!canAccessCategory(idUtilisateur, idCategorie)) {
            Utilisateur user = getUtilisateur(idUtilisateur);
            List<Integer> allowed = getAllowedCategories(idUtilisateur);
            throw new SecurityException(
                "Action non autorisée: " + action + ". " +
                "Utilisateur " + user.getNom() + " n'a pas accès à la catégorie " + idCategorie + ". " +
                "Catégories autorisées: " + allowed
            );
        }
    }
    
    /**
     * Require user to have access to an article
     */
    public void requireArticleAccess(Integer idUtilisateur, Integer idArticle, String action) {
        if (!canAccessArticle(idUtilisateur, idArticle)) {
            Utilisateur user = getUtilisateur(idUtilisateur);
            Article article = articleRepository.findById(idArticle).orElse(null);
            List<Integer> allowed = getAllowedCategories(idUtilisateur);
            throw new SecurityException(
                "Action non autorisée: " + action + ". " +
                "Utilisateur " + user.getNom() + " n'a pas accès à l'article '" + 
                (article != null ? article.getLibelle() : idArticle) + "' (catégorie " + 
                (article != null ? article.getIdCategorie() : "?") + "). " +
                "Catégories autorisées: " + allowed
            );
        }
    }
    
    /**
     * Validate access to multiple articles
     */
    public void requireArticlesAccess(Integer idUtilisateur, List<Integer> articleIds, String action) {
        for (Integer idArticle : articleIds) {
            requireArticleAccess(idUtilisateur, idArticle, action);
        }
    }
    
    // ========================================
    // FOURNISSEUR RESTRICTIONS
    // ========================================
    
    /**
     * Check if user has fournisseur restrictions
     */
    public boolean hasFournisseurRestrictions(Integer idUtilisateur) {
        if (isDirecteurLevel(idUtilisateur)) return false;
        return restrictionFournisseurRepository.hasRestrictions(idUtilisateur);
    }
    
    /**
     * Get list of allowed fournisseur IDs for user (empty = all allowed)
     */
    public List<Integer> getAllowedFournisseurs(Integer idUtilisateur) {
        if (isDirecteurLevel(idUtilisateur)) return List.of();
        return restrictionFournisseurRepository.findAllowedFournisseursByUtilisateur(idUtilisateur);
    }
    
    /**
     * Check if user can work with a specific fournisseur
     */
    public boolean canAccessFournisseur(Integer idUtilisateur, Integer idFournisseur) {
        if (isDirecteurLevel(idUtilisateur)) return true;
        if (!hasFournisseurRestrictions(idUtilisateur)) return true;
        return restrictionFournisseurRepository.isAllowedForFournisseur(idUtilisateur, idFournisseur);
    }
    
    /**
     * Require user to have access to a fournisseur
     */
    public void requireFournisseurAccess(Integer idUtilisateur, Integer idFournisseur, String action) {
        if (idFournisseur == null) return; // No fournisseur specified, no check needed
        
        if (!canAccessFournisseur(idUtilisateur, idFournisseur)) {
            Utilisateur user = getUtilisateur(idUtilisateur);
            List<Integer> allowed = getAllowedFournisseurs(idUtilisateur);
            throw new SecurityException(
                "Action non autorisée: " + action + ". " +
                "Utilisateur " + user.getNom() + " n'a pas accès au fournisseur " + idFournisseur + ". " +
                "Fournisseurs autorisés: " + allowed
            );
        }
    }
    
    // ========================================
    // HISTORIQUE GENERAL
    // ========================================
    
    /**
     * Log an action to historique_general
     */
    public HistoriqueGeneral logAction(Integer idUtilisateur, String nomTable, String description, Integer entityId) {
        HistoriqueGeneral hg = new HistoriqueGeneral();
        hg.setDateHistorique(LocalDateTime.now());
        hg.setNomTable(nomTable);
        hg.setDesc(description);
        hg.setId(entityId);
        hg.setIdUtilisateur(idUtilisateur);
        
        return historiqueGeneralRepository.save(hg);
    }
    
    /**
     * Log an action with custom date
     */
    public HistoriqueGeneral logAction(Integer idUtilisateur, String nomTable, String description, Integer entityId, LocalDateTime date) {
        HistoriqueGeneral hg = new HistoriqueGeneral();
        hg.setDateHistorique(date != null ? date : LocalDateTime.now());
        hg.setNomTable(nomTable);
        hg.setDesc(description);
        hg.setId(entityId);
        hg.setIdUtilisateur(idUtilisateur);
        
        return historiqueGeneralRepository.save(hg);
    }
    
    // ========================================
    // COMBINED BUSINESS RULE CHECKS
    // ========================================
    
    /**
     * Full authorization check for creating a demande d'achat
     * - Must be in Ventes department
     * - Must have access to all articles
     */
    public void authorizeDemandeAchat(Integer idUtilisateur, List<Integer> articleIds) {
        requireVentesDept(idUtilisateur, "Créer demande d'achat");
        requireArticlesAccess(idUtilisateur, articleIds, "Créer demande d'achat");
    }
    
    /**
     * Full authorization check for creating a proforma
     * - Must be in Ventes department
     * - Must have access to fournisseur (if applicable)
     * - Must have access to all articles
     */
    public void authorizeCreerProforma(Integer idUtilisateur, Integer idFournisseur, List<Integer> articleIds) {
        requireVentesDept(idUtilisateur, "Créer proforma");
        requireFournisseurAccess(idUtilisateur, idFournisseur, "Créer proforma");
        requireArticlesAccess(idUtilisateur, articleIds, "Créer proforma");
    }
    
    /**
     * Full authorization check for validating a proforma
     * - Must be in Ventes department
     * - Must have niveau >= 7
     */
    public void authorizeValiderProforma(Integer idUtilisateur) {
        requireVentesAndValidation(idUtilisateur, "Valider proforma");
    }
    
    /**
     * Full authorization check for creating a commande
     * - Must be in Ventes department
     */
    public void authorizeCreerCommande(Integer idUtilisateur) {
        requireVentesDept(idUtilisateur, "Créer commande");
    }
    
    /**
     * Full authorization check for validating a commande
     * - Must be in Ventes department
     * - Must have niveau >= 7
     */
    public void authorizeValiderCommande(Integer idUtilisateur) {
        requireVentesAndValidation(idUtilisateur, "Valider commande");
    }
    
    /**
     * Full authorization check for delivering a commande
     * - Must be in Ventes department
     */
    public void authorizeLivraison(Integer idUtilisateur) {
        requireVentesDept(idUtilisateur, "Effectuer livraison");
    }
    
    /**
     * Full authorization check for accessing valorisation stock
     * - Must be in Finance department
     */
    public void authorizeValorisationStock(Integer idUtilisateur) {
        requireFinanceDept(idUtilisateur, "Accéder à la valorisation de stock");
    }

}
