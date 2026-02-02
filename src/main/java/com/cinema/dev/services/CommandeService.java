package com.cinema.dev.services;

import com.cinema.dev.dtos.ValidationStatusDTO;
import com.cinema.dev.forms.MvtStockForm;
import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandeService {
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private CommandeEtatRepository commandeEtatRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ProformaEtatRepository proformaEtatRepository;
    
    @Autowired
    private PaiementService paiementService;
    
    @Autowired
    private LivraisonRepository livraisonRepository;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private ValidationService validationService;

     @Autowired
    private ProformaDetailRepository proformaDetailRepository;

     @Autowired
    private MvtStockService mvtStockService;
    
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }
    
    public List<Commande> findWithFilters(Integer idProforma, LocalDateTime startDate, LocalDateTime endDate) {
        return commandeRepository.findWithFilters(idProforma, startDate, endDate);
    }

    /**
     * Créer une commande
     * 
     * Authorization:
     * - Must be in 'Ventes' department
     */
    @Transactional
    public Commande creerCommande(Integer idUtilisateur, Integer idProforma, LocalDateTime dateCommande) {
        //* -- Authorization check
        authorizationService.authorizeCreerCommande(idUtilisateur);
        
        //* -- Get proforma
        Proforma proforma = proformaRepository.findById(idProforma)
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Use provided date or current date
        LocalDateTime dateCmd = dateCommande != null ? dateCommande : LocalDateTime.now();
        
        //? === RG: Validation ===
        //* -- Date proforma still valid (current < date_fin)
        if (dateCmd.isAfter(proforma.getDateFin())) {
            throw new IllegalArgumentException("Proforma expires at " + proforma.getDateFin() + ". Input: " + dateCommande);
        }
        
        //* -- Proforma etat is valid (etat = 2)
        if (!proformaEtatRepository.existsByIdProformaAndIdEtat(idProforma, 2)) {
            throw new IllegalArgumentException("Proforma is not validated");
        }
        
        //* -- Create commande
        Commande commande = new Commande();
        commande.setIdProforma(idProforma);
        commande.setDate(dateCmd);
        
        //* -- RG: Date commande after date proforma
        if (commande.getDate().isBefore(proforma.getDateDebut())) {
            throw new IllegalArgumentException("Commande date must be after proforma date");
        }
        
        //* -- Insert commande
        Commande savedCommande = commandeRepository.save(commande);
        
        //* -- Insert commande_etat (cree = 1)
        CommandeEtat commandeEtat = new CommandeEtat(savedCommande.getIdCommande(), 1, dateCmd);
        commandeEtatRepository.save(commandeEtat);
        
        //* -- Log to historique
        authorizationService.logAction(idUtilisateur, "commande", "Création commande", savedCommande.getIdCommande(), dateCmd);
        
        return savedCommande;
    }
    
    /**
     * Legacy method without authorization (for backward compatibility)
     * @deprecated Use creerCommande(Integer idUtilisateur, ...) instead
     */
    @Deprecated
    @Transactional
    public Commande creerCommande(Integer idProforma, LocalDateTime dateCommande) {
        //* -- Get proforma
        Proforma proforma = proformaRepository.findById(idProforma)
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Use provided date or current date
        LocalDateTime dateCmd = dateCommande != null ? dateCommande : LocalDateTime.now();
        
        //? === RG: Validation ===
        //* -- Date proforma still valid (current < date_fin)
        if (dateCmd.isAfter(proforma.getDateFin())) {
            throw new IllegalArgumentException("Proforma expires at " + proforma.getDateFin() + ". Input: " + dateCommande);
        }
        
        //* -- Proforma etat is valid (etat = 2)
        if (!proformaEtatRepository.existsByIdProformaAndIdEtat(idProforma, 2)) {
            throw new IllegalArgumentException("Proforma is not validated");
        }
        
        //* -- Create commande
        Commande commande = new Commande();
        commande.setIdProforma(idProforma);
        commande.setDate(dateCmd);
        
        //* -- RG: Date commande after date proforma
        if (commande.getDate().isBefore(proforma.getDateDebut())) {
            throw new IllegalArgumentException("Commande date must be after proforma date");
        }
        
        //* -- Insert commande
        Commande savedCommande = commandeRepository.save(commande);
        
        //* -- Insert commande_etat (cree = 1)
        CommandeEtat commandeEtat = new CommandeEtat(savedCommande.getIdCommande(), 1, dateCmd);
        commandeEtatRepository.save(commandeEtat);
        
        return savedCommande;
    }
    
    /**
     * Valider une commande (multi-step validation)
     * 
     * Authorization:
     * - Must be in 'Ventes' department
     * - Must have required niveau for the next step
     * - Cannot validate if already validated a step for this commande
     * 
     * @return ValidationStatusDTO with current validation status
     */
    @Transactional
    public ValidationStatusDTO validerCommande(Integer idUtilisateur, Integer idCommande, LocalDateTime dateValidation) {
        //* -- Check if commande exists
        commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        //* -- Delegate to ValidationService for multi-step validation
        return validationService.validate(idUtilisateur, ValidationService.ENTITY_COMMANDE, 
                                          idCommande, dateValidation);
    }
    
    /**
     * Get the validation status of a commande
     */
    public ValidationStatusDTO getValidationStatus(Integer idCommande) {
        return validationService.getValidationStatus(ValidationService.ENTITY_COMMANDE, idCommande);
    }
    
    /**
     * Check if commande is fully validated
     */
    public boolean isFullyValidated(Integer idCommande) {
        return validationService.isFullyValidated(ValidationService.ENTITY_COMMANDE, idCommande);
    }
    
    /**
     * Legacy method without authorization (for backward compatibility)
     * @deprecated Use validerCommande(Integer idUtilisateur, ...) instead
     */
    @Deprecated
    @Transactional
    public CommandeEtat validerCommande(Integer idCommande, LocalDateTime dateValidation) {
        //* -- Check if commande exists
        commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        //* -- Insert commande_etat (valide = 2)
        LocalDateTime date = dateValidation != null ? dateValidation : LocalDateTime.now();
        CommandeEtat commandeEtat = new CommandeEtat(idCommande, 2, date);
        
        return commandeEtatRepository.save(commandeEtat);
    }
    
    /**
     * Livrer une commande
     * 
     * Authorization:
     * - Must be in 'Ventes' department
     * @throws Exception 
     */
    @Transactional(rollbackFor = Exception.class)
    public Livraison livrerCommande(Integer idUtilisateur, Integer idCommande, LocalDateTime dateLivraison) throws Exception {
        //* -- Authorization check
        authorizationService.authorizeLivraison(idUtilisateur);
        
        //* -- Check if commande exists
        @SuppressWarnings("unused")
        Commande commande = commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        //* -- Check if commande is validated
        if (!commandeEtatRepository.existsByIdCommandeAndIdEtat(idCommande, 2)) {
            throw new IllegalArgumentException("Commande must be validated before delivery");
        }
        
        //* -- Check if commande is fully paid
        BigDecimal reste = paiementService.getMontantTotalPourUneCommande(idCommande, null);
        if (reste.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Commande must be fully paid before delivery. Remaining: " + reste);
        }
        
        //* -- Create livraison
        LocalDateTime date = dateLivraison != null ? dateLivraison : LocalDateTime.now();
        Livraison livraison = new Livraison();
        livraison.setIdCommande(idCommande);
        livraison.setDate(date);
        
        Livraison saved = livraisonRepository.save(livraison);
        
        faireMouvoirStockApresLivraison(saved, commande);
        //* -- Log to historique
        authorizationService.logAction(idUtilisateur, "livraison", "Livraison commande", saved.getIdLivraison(), date);
        
        return saved;
    }

    public MvtStock faireMouvoirStockApresLivraison(Livraison livraison, Commande commande) throws Exception {
        MvtStockForm mvtStockForm = new MvtStockForm();
            List<ProformaDetail> listeArticlesDetails = this.proformaDetailRepository.findDetailsByCommandeId(commande.getIdCommande());
            HashMap<Integer, Integer> articleQte =  (HashMap<Integer, Integer>) ProformaDetail.mapQuantiteParArticleStream(listeArticlesDetails);
            System.out.println("----------------"); 
            for (Integer idArticle : articleQte.keySet()) {
            //    Integer qte = mvtStockForm.getArticleQte().get(idArticle);
               System.out.println("id  = " + idArticle);
             }

            mvtStockForm.setArticleQte(articleQte);
            mvtStockForm.setDate(livraison.getDate().toLocalDate());
            mvtStockForm.setDescriptionQualite("OK");
            mvtStockForm.setIdLivraison(livraison.getIdLivraison());
            mvtStockForm.setIdDepot(null);
            mvtStockForm.setIdAjustement(null);
            mvtStockForm.setDesignation(null);
            mvtStockForm.setIdDepot(2);
            MvtStock mvtStock = null;
            if(estCommandeEntrante(livraison.getIdCommande())) {
                // Entreante
                System.out.println("entrante");
                mvtStockForm.setEntrant(true);
                mvtStock = this.mvtStockService.creerMvtStockEntree(mvtStockForm);
            } else {
                System.out.println("sortante");
                mvtStockForm.setEntrant(false);
                mvtStock = this.mvtStockService.creeerMvtStockSortie(mvtStockForm);
            }
            return mvtStock;
    }

    /**
     * @return true si Entrante (idClient null), false si Sortante (idFournisseur null)
     */
    public boolean estCommandeEntrante(Integer idCommande) {
        List<Object[]> results = commandeRepository.findProformaDetailsByCommandeId(idCommande);

        if (results.isEmpty()) {
            throw new RuntimeException("Commande ou Proforma associé introuvable.");
        }

        Object[] row = results.get(0);
        Integer idClient = (Integer) row[0];
        Integer idFournisseur = (Integer) row[1];

        // Règle : Entrante si idClient est null (on achète au fournisseur)
        if (idClient == null) {
            return true; 
        } 
        // Sortante si idFournisseur est null (on vend au client)
        else if (idFournisseur == null) {
            return false;
        }

        throw new IllegalStateException("Le proforma doit avoir soit un client null, soit un fournisseur null.");
    }

    /**
     * Legacy method without authorization (for backward compatibility)
     * @deprecated Use livrerCommande(Integer idUtilisateur, ...) instead
     */
    @Deprecated
    @Transactional
    public Livraison livrerCommande(Integer idCommande, LocalDateTime dateLivraison) {
        //* -- Check if commande exists
        @SuppressWarnings("unused")
        Commande commande = commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        //* -- Check if commande is validated
        if (!commandeEtatRepository.existsByIdCommandeAndIdEtat(idCommande, 2)) {
            throw new IllegalArgumentException("Commande must be validated before delivery");
        }
        
        //* -- Check if commande is fully paid
        BigDecimal reste = paiementService.getMontantTotalPourUneCommande(idCommande, null);
        if (reste.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Commande must be fully paid before delivery. Remaining: " + reste);
        }
        
        //* -- Create livraison
        Livraison livraison = new Livraison();
        livraison.setIdCommande(idCommande);
        livraison.setDate(dateLivraison != null ? dateLivraison : LocalDateTime.now());
        
        return livraisonRepository.save(livraison);
    }
}