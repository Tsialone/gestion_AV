package com.cinema.dev.services;

import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
     * Valider une commande
     * 
     * Authorization:
     * - Must be in 'Ventes' department
     * - Must have niveau >= 7
     */
    @Transactional
    public CommandeEtat validerCommande(Integer idUtilisateur, Integer idCommande, LocalDateTime dateValidation) {
        //* -- Authorization check
        authorizationService.authorizeValiderCommande(idUtilisateur);
        
        //* -- Check if commande exists
        commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        //* -- Insert commande_etat (valide = 2)
        LocalDateTime date = dateValidation != null ? dateValidation : LocalDateTime.now();
        CommandeEtat commandeEtat = new CommandeEtat(idCommande, 2, date);
        CommandeEtat saved = commandeEtatRepository.save(commandeEtat);
        
        //* -- Log to historique
        authorizationService.logAction(idUtilisateur, "commande_etat", "Validation commande", idCommande, date);
        
        return saved;
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
     */
    @Transactional
    public Livraison livrerCommande(Integer idUtilisateur, Integer idCommande, LocalDateTime dateLivraison) {
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
        
        //* -- Log to historique
        authorizationService.logAction(idUtilisateur, "livraison", "Livraison commande", saved.getIdLivraison(), date);
        
        return saved;
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