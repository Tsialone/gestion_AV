package com.cinema.dev.services;

import com.cinema.dev.dtos.ValidationStatusDTO;
import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProformaService {
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ProformaDetailRepository proformaDetailRepository;
    
    @Autowired
    private ProformaEtatRepository proformaEtatRepository;
    
    @Autowired
    private DemandeAchatRepository demandeAchatRepository;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private ValidationService validationService;
    
    public List<Proforma> findAll() {
        return proformaRepository.findAll();
    }
    
    public List<Proforma> findWithFilters(Integer idClient, Integer idFournisseur, LocalDateTime startDate, LocalDateTime endDate) {
        return proformaRepository.findWithFilters(idClient, idFournisseur, startDate, endDate);
    }
    
    /**
     * Créer un proforma
     * 
     * Authorization:
     * - Must be in 'Ventes' department
     * - Must have access to fournisseur (if applicable)
     * - Must have access to all articles (category restrictions)
     */
    @Transactional
    public Proforma creerProforma(Integer idUtilisateur, Integer idDemandeAchat, Integer idClient, Integer idFournisseur, 
                                  Proforma proforma, ProformaDetail[] proformaDetails, LocalDateTime dateCreation) {
        //* -- Authorization check
        List<Integer> articleIds = Arrays.stream(proformaDetails)
            .map(d -> d.getId().getIdArticle())
            .collect(Collectors.toList());
        authorizationService.authorizeCreerProforma(idUtilisateur, idFournisseur, articleIds);
        
        //? === RG: Validation ===
        //* -- idFournisseur null => idClient not null and vice versa
        if ((idFournisseur == null && idClient == null) || (idFournisseur != null && idClient != null)) {
            throw new IllegalArgumentException("Either idClient or idFournisseur must be provided, but not both");
        }
        
        //* -- If idClient in demandeAchat not null, idClient in proforma must be the same
        DemandeAchat da = demandeAchatRepository.findById(idDemandeAchat)
            .orElseThrow(() -> new IllegalArgumentException("DemandeAchat not found"));
        
        if (da.getIdClient() != null && !da.getIdClient().equals(idClient)) {
            throw new IllegalArgumentException("idClient in proforma must match idClient in demande_achat");
        }
        
        //* -- Set relationships
        proforma.setIdDa(idDemandeAchat);
        proforma.setIdClient(idClient);
        proforma.setIdFournisseur(idFournisseur);
        
        //* -- Insert proforma
        Proforma savedProforma = proformaRepository.save(proforma);
        
        //* -- Insert proforma_detail
        for (ProformaDetail detail : proformaDetails) {
            detail.getId().setIdProforma(savedProforma.getIdProforma());
            proformaDetailRepository.save(detail);
        }
        
        //* -- Insert proforma_etat (cree = 1)
        LocalDateTime date = dateCreation != null ? dateCreation : LocalDateTime.now();
        ProformaEtat proformaEtat = new ProformaEtat(savedProforma.getIdProforma(), 1, date);
        proformaEtatRepository.save(proformaEtat);
        
        //* -- Log to historique
        authorizationService.logAction(idUtilisateur, "proforma", "Création proforma", savedProforma.getIdProforma(), date);
        
        return savedProforma;
    }
    
    /**
     * Legacy method without authorization (for backward compatibility)
     * @deprecated Use creerProforma(Integer idUtilisateur, ...) instead
     */
    @Deprecated
    @Transactional
    public Proforma creerProforma(Integer idDemandeAchat, Integer idClient, Integer idFournisseur, 
                                  Proforma proforma, ProformaDetail[] proformaDetails, LocalDateTime dateCreation) {
        //? === RG: Validation ===
        //* -- idFournisseur null => idClient not null and vice versa
        if ((idFournisseur == null && idClient == null) || (idFournisseur != null && idClient != null)) {
            throw new IllegalArgumentException("Either idClient or idFournisseur must be provided, but not both");
        }
        
        //* -- If idClient in demandeAchat not null, idClient in proforma must be the same
        DemandeAchat da = demandeAchatRepository.findById(idDemandeAchat)
            .orElseThrow(() -> new IllegalArgumentException("DemandeAchat not found"));
        
        if (da.getIdClient() != null && !da.getIdClient().equals(idClient)) {
            throw new IllegalArgumentException("idClient in proforma must match idClient in demande_achat");
        }
        
        //* -- Set relationships
        proforma.setIdDa(idDemandeAchat);
        proforma.setIdClient(idClient);
        proforma.setIdFournisseur(idFournisseur);
        
        //* -- Insert proforma
        Proforma savedProforma = proformaRepository.save(proforma);
        
        //* -- Insert proforma_detail
        for (ProformaDetail detail : proformaDetails) {
            detail.getId().setIdProforma(savedProforma.getIdProforma());
            proformaDetailRepository.save(detail);
        }
        
        //* -- Insert proforma_etat (cree = 1)
        LocalDateTime date = dateCreation != null ? dateCreation : LocalDateTime.now();
        ProformaEtat proformaEtat = new ProformaEtat(savedProforma.getIdProforma(), 1, date);
        proformaEtatRepository.save(proformaEtat);
        
        return savedProforma;
    }
    
    /**
     * Valider un proforma (multi-step validation)
     * 
     * Authorization:
     * - Must be in 'Ventes' department
     * - Must have required niveau for the next step
     * - Cannot validate if already validated a step for this proforma
     * 
     * @return ValidationStatusDTO with current validation status
     */
    @Transactional
    public ValidationStatusDTO validerProforma(Integer idUtilisateur, Integer idProforma, LocalDateTime dateValidation) {
        //* -- Check if proforma exists
        proformaRepository.findById(idProforma)
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Delegate to ValidationService for multi-step validation
        return validationService.validate(idUtilisateur, ValidationService.ENTITY_PROFORMA, idProforma, dateValidation);
    }
    
    /**
     * Get the validation status of a proforma
     */
    public ValidationStatusDTO getValidationStatus(Integer idProforma) {
        return validationService.getValidationStatus(ValidationService.ENTITY_PROFORMA, idProforma);
    }
    
    /**
     * Check if proforma is fully validated
     */
    public boolean isFullyValidated(Integer idProforma) {
        return validationService.isFullyValidated(ValidationService.ENTITY_PROFORMA, idProforma);
    }
    
    /**
     * Legacy method without authorization (for backward compatibility)
     * @deprecated Use validerProforma(Integer idUtilisateur, ...) instead
     */
    @Deprecated
    @Transactional
    public ProformaEtat validerProforma(Integer idProforma, LocalDateTime dateValidation) {
        //* -- Check if proforma exists
        proformaRepository.findById(idProforma)
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Insert proforma_etat (valide = 2)
        LocalDateTime date = dateValidation != null ? dateValidation : LocalDateTime.now();
        ProformaEtat proformaEtat = new ProformaEtat(idProforma, 2, date);
        
        return proformaEtatRepository.save(proformaEtat);
    }
}