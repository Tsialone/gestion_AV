package com.cinema.dev.services;

import com.cinema.dev.dtos.DeptUsersDTO;
import com.cinema.dev.dtos.UtilisateurDisplayDTO;
import com.cinema.dev.dtos.UtilisateurSessionDTO;
import com.cinema.dev.models.Dept;
import com.cinema.dev.models.Role;
import com.cinema.dev.models.Utilisateur;
import com.cinema.dev.models.Categorie;
import com.cinema.dev.models.Fournisseur;
import com.cinema.dev.repositories.DeptRepository;
import com.cinema.dev.repositories.RoleRepository;
import com.cinema.dev.repositories.UtilisateurRepository;
import com.cinema.dev.repositories.CategorieRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.RestrictionCategorieRepository;
import com.cinema.dev.repositories.RestrictionFournisseurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private static final String SESSION_USER_KEY = "currentUser";

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private RestrictionCategorieRepository restrictionCategorieRepository;

    @Autowired
    private RestrictionFournisseurRepository restrictionFournisseurRepository;

    /**
     * Connecte un utilisateur en le stockant dans la session
     */
    public UtilisateurSessionDTO login(HttpSession session, Integer idUtilisateur) {
        Utilisateur user = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + idUtilisateur));

        Role role = roleRepository.findById(user.getIdRole())
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé: " + user.getIdRole()));

        Dept dept = deptRepository.findById(user.getIdDept())
                .orElseThrow(() -> new RuntimeException("Département non trouvé: " + user.getIdDept()));

        // Get restriction data
        List<String> restrictedCategories = new ArrayList<>();
        List<String> restrictedFournisseurs = new ArrayList<>();

        // Get category restrictions
        List<Integer> categoryIds = restrictionCategorieRepository.findAllowedCategoriesByUtilisateur(idUtilisateur);
        if (!categoryIds.isEmpty()) {
            for (Integer catId : categoryIds) {
                categorieRepository.findById(catId).ifPresent(cat -> 
                    restrictedCategories.add(cat.getLibelle())
                );
            }
        }

        // Get fournisseur restrictions
        List<Integer> fournisseurIds = restrictionFournisseurRepository.findAllowedFournisseursByUtilisateur(idUtilisateur);
        if (!fournisseurIds.isEmpty()) {
            for (Integer fournId : fournisseurIds) {
                fournisseurRepository.findById(fournId).ifPresent(fourn -> 
                    restrictedFournisseurs.add(fourn.getNom())
                );
            }
        }

        UtilisateurSessionDTO sessionUser = new UtilisateurSessionDTO(
                user.getIdUtilisateur(),
                user.getNom(),
                role.getNom(),
                role.getNiveau(),
                dept.getNom(),
                dept.getIdDept(),
                role.getIdRole(),
                restrictedCategories,
                restrictedFournisseurs
        );

        session.setAttribute(SESSION_USER_KEY, sessionUser);
        return sessionUser;
    }

    /**
     * Récupère l'utilisateur connecté depuis la session
     */
    public UtilisateurSessionDTO getCurrentUser(HttpSession session) {
        return (UtilisateurSessionDTO) session.getAttribute(SESSION_USER_KEY);
    }

    /**
     * Vérifie si un utilisateur est connecté
     */
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }

    /**
     * Déconnecte l'utilisateur (supprime de la session)
     */
    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        session.invalidate();
    }

    /**
     * Récupère l'ID de l'utilisateur connecté
     */
    public Integer getCurrentUserId(HttpSession session) {
        UtilisateurSessionDTO user = getCurrentUser(session);
        return user != null ? user.getIdUtilisateur() : null;
    }

    /**
     * Récupère tous les utilisateurs pour le sélecteur
     */
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    
    /**
     * Récupère tous les utilisateurs groupés par département, triés par niveau décroissant
     */
    public List<DeptUsersDTO> getUtilisateursGroupedByDept() {
        List<Utilisateur> allUsers = utilisateurRepository.findAll();
        List<Dept> allDepts = deptRepository.findAll();
        Map<Integer, Role> rolesMap = roleRepository.findAll().stream()
                .collect(Collectors.toMap(Role::getIdRole, r -> r));
        Map<Integer, String> deptNamesMap = allDepts.stream()
                .collect(Collectors.toMap(Dept::getIdDept, Dept::getNom));
        
        // Group users by dept
        Map<Integer, List<Utilisateur>> usersByDept = allUsers.stream()
                .collect(Collectors.groupingBy(Utilisateur::getIdDept));
        
        List<DeptUsersDTO> result = new ArrayList<>();
        
        // Sort depts by id for consistent ordering
        List<Integer> sortedDeptIds = allDepts.stream()
                .map(Dept::getIdDept)
                .sorted()
                .collect(Collectors.toList());
        
        for (Integer deptId : sortedDeptIds) {
            List<Utilisateur> deptUsers = usersByDept.getOrDefault(deptId, new ArrayList<>());
            if (deptUsers.isEmpty()) continue;
            
            // Convert to display DTOs and sort by niveau descending
            List<UtilisateurDisplayDTO> displayUsers = deptUsers.stream()
                    .map(u -> {
                        Role role = rolesMap.get(u.getIdRole());
                        return new UtilisateurDisplayDTO(
                                u.getIdUtilisateur(),
                                u.getNom(),
                                role != null ? role.getNiveau() : 0,
                                deptNamesMap.get(u.getIdDept()),
                                u.getIdDept()
                        );
                    })
                    .sorted(Comparator.comparing(UtilisateurDisplayDTO::getNiveau).reversed()
                            .thenComparing(UtilisateurDisplayDTO::getNom))
                    .collect(Collectors.toList());
            
            result.add(new DeptUsersDTO(deptId, deptNamesMap.get(deptId), displayUsers));
        }
        
        return result;
    }
}
