package com.cinema.dev.services;

import com.cinema.dev.dtos.ValidationStatusDTO;
import com.cinema.dev.dtos.ValidationStatusDTO.StepDetail;
import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for multi-step hierarchical validation.
 * 
 * Rules:
 * - Validation is sequential: step 1 must complete before step 2
 * - Same user cannot validate multiple steps for the same entity
 * - User must have required niveau for each step
 * - Entity is fully validated only when all required steps are complete
 */
@Service
public class ValidationService {
    
    public static final String ENTITY_PROFORMA = "proforma";
    public static final String ENTITY_COMMANDE = "commande";
    
    @Autowired
    private ValidationStepRepository validationStepRepository;
    
    @Autowired
    private ConfVaRepository confVaRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ProformaEtatRepository proformaEtatRepository;
    
    @Autowired
    private CommandeEtatRepository commandeEtatRepository;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    /**
     * Get the validation configuration for an entity type
     */
    public ConfVa getValidationConfig(String entityType) {
        return confVaRepository.findByLibelle(entityType)
            .orElseThrow(() -> new IllegalStateException(
                "No validation config found for entity type: " + entityType + 
                ". Please insert into conf_va table."));
    }
    
    /**
     * Get the user's niveau
     */
    public int getUserNiveau(Integer idUtilisateur) {
        Utilisateur user = utilisateurRepository.findById(idUtilisateur)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + idUtilisateur));
        Role role = roleRepository.findById(user.getIdRole())
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        return role.getNiveau();
    }
    
    /**
     * Get the full validation status for an entity
     */
    public ValidationStatusDTO getValidationStatus(String entityType, Integer entityId) {
        ConfVa config = getValidationConfig(entityType);
        
        int totalStepsRequired = config.getNiveau2() > 0 ? 2 : 1;
        
        List<ValidationStep> existingSteps = validationStepRepository
            .findByEntityTypeAndEntityIdOrderByStepNumber(entityType, entityId);
        
        // Build step details
        List<StepDetail> stepDetails = new ArrayList<>();
        for (int i = 1; i <= totalStepsRequired; i++) {
            final int stepNumber = i;
            StepDetail detail = new StepDetail();
            detail.setStepNumber(i);
            detail.setRequiredNiveau(i == 1 ? config.getNiveau1() : config.getNiveau2());
            
            // Find if this step is completed
            Optional<ValidationStep> completed = existingSteps.stream()
                .filter(s -> s.getStepNumber() == stepNumber)
                .findFirst();
            
            if (completed.isPresent()) {
                detail.setCompleted(true);
                detail.setValidatedByUserId(completed.get().getIdUtilisateur());
                detail.setValidatedAt(completed.get().getValidatedAt());
                
                // Get user name
                utilisateurRepository.findById(completed.get().getIdUtilisateur())
                    .ifPresent(u -> detail.setValidatedByUserName(u.getNom()));
            } else {
                detail.setCompleted(false);
            }
            
            stepDetails.add(detail);
        }
        
        int stepsCompleted = (int) stepDetails.stream().filter(StepDetail::isCompleted).count();
        boolean isFullyValidated = stepsCompleted >= totalStepsRequired;
        
        // Determine next step
        Integer nextStepNumber = null;
        Integer nextStepRequiredNiveau = null;
        if (!isFullyValidated) {
            nextStepNumber = stepsCompleted + 1;
            nextStepRequiredNiveau = nextStepNumber == 1 ? config.getNiveau1() : config.getNiveau2();
        }
        
        ValidationStatusDTO status = new ValidationStatusDTO();
        status.setEntityType(entityType);
        status.setEntityId(entityId);
        status.setRequiredNiveau1(config.getNiveau1());
        status.setRequiredNiveau2(config.getNiveau2());
        status.setTotalStepsRequired(totalStepsRequired);
        status.setStepsCompleted(stepsCompleted);
        status.setFullyValidated(isFullyValidated);
        status.setNextStepNumber(nextStepNumber);
        status.setNextStepRequiredNiveau(nextStepRequiredNiveau);
        status.setSteps(stepDetails);
        
        return status;
    }
    
    /**
     * Validate an entity (proforma or commande).
     * This performs the next validation step if the user is authorized.
     * 
     * @return ValidationStatusDTO with updated status
     * @throws IllegalArgumentException if validation is not allowed
     */
    @Transactional
    public ValidationStatusDTO validate(Integer idUtilisateur, String entityType, Integer entityId, 
                                         LocalDateTime validationDate) {
        // Check user is in Ventes department
        authorizationService.requireDepartement(idUtilisateur, "Ventes", "Valider " + entityType);
        
        // Get current status
        ValidationStatusDTO status = getValidationStatus(entityType, entityId);
        
        // Check if already fully validated
        if (status.isFullyValidated()) {
            throw new IllegalArgumentException(
                entityType + " #" + entityId + " est deja entierement valide.");
        }
        
        // Check if user has already validated a step
        if (validationStepRepository.existsByEntityTypeAndEntityIdAndIdUtilisateur(
                entityType, entityId, idUtilisateur)) {
            throw new IllegalArgumentException(
                "Vous avez deja valide une etape pour ce " + entityType + 
                ". Un autre utilisateur doit valider l'etape suivante.");
        }
        
        // Check user has required niveau
        int userNiveau = getUserNiveau(idUtilisateur);
        if (userNiveau < status.getNextStepRequiredNiveau()) {
            throw new IllegalArgumentException(
                "Niveau insuffisant pour valider. " +
                "Votre niveau: " + userNiveau + ", " +
                "Niveau requis pour etape " + status.getNextStepNumber() + ": " + status.getNextStepRequiredNiveau());
        }
        
        // Create validation step
        LocalDateTime date = validationDate != null ? validationDate : LocalDateTime.now();
        ValidationStep step = new ValidationStep(
            entityType, entityId, status.getNextStepNumber(), idUtilisateur, date);
        validationStepRepository.save(step);
        
        // Log action
        authorizationService.logAction(idUtilisateur, "validation_step", 
            "Validation " + entityType + " etape " + status.getNextStepNumber(), entityId, date);
        
        // Check if this completes validation
        ValidationStatusDTO newStatus = getValidationStatus(entityType, entityId);
        
        if (newStatus.isFullyValidated()) {
            // Update entity etat to "Valide" (etat = 2)
            if (ENTITY_PROFORMA.equals(entityType)) {
                ProformaEtat etat = new ProformaEtat(entityId, 2, date);
                proformaEtatRepository.save(etat);
                authorizationService.logAction(idUtilisateur, "proforma_etat", 
                    "Proforma entierement valide", entityId, date);
            } else if (ENTITY_COMMANDE.equals(entityType)) {
                CommandeEtat etat = new CommandeEtat(entityId, 2, date);
                commandeEtatRepository.save(etat);
                authorizationService.logAction(idUtilisateur, "commande_etat", 
                    "Commande entierement validee", entityId, date);
            }
        }
        
        return newStatus;
    }
    
    /**
     * Check if an entity is fully validated
     */
    public boolean isFullyValidated(String entityType, Integer entityId) {
        return getValidationStatus(entityType, entityId).isFullyValidated();
    }
    
    /**
     * Check if a specific step is completed
     */
    public boolean isStepCompleted(String entityType, Integer entityId, int stepNumber) {
        return validationStepRepository.existsByEntityTypeAndEntityIdAndStepNumber(
            entityType, entityId, stepNumber);
    }
}
