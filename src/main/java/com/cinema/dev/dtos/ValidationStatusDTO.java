package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing the validation status of an entity (proforma/commande)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationStatusDTO {
    
    private String entityType;
    private Integer entityId;
    
    // Configuration
    private Integer requiredNiveau1;
    private Integer requiredNiveau2;  // 0 means no step 2 required
    private int totalStepsRequired;   // 1 or 2
    
    // Current status
    private int stepsCompleted;
    private boolean isFullyValidated;
    private Integer nextStepNumber;   // null if fully validated
    private Integer nextStepRequiredNiveau;  // null if fully validated
    
    // Step details
    private List<StepDetail> steps;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepDetail {
        private int stepNumber;
        private Integer requiredNiveau;
        private boolean isCompleted;
        private Integer validatedByUserId;
        private String validatedByUserName;
        private LocalDateTime validatedAt;
    }
    
    /**
     * Check if a user can validate the next step
     * @param userNiveau the user's niveau
     * @param userId the user's id
     * @return true if user can validate
     */
    public boolean canUserValidate(int userNiveau, Integer userId) {
        if (isFullyValidated) return false;
        if (nextStepRequiredNiveau == null) return false;
        if (userNiveau < nextStepRequiredNiveau) return false;
        
        // Check if user has already validated any step
        if (steps != null) {
            for (StepDetail step : steps) {
                if (step.isCompleted && userId.equals(step.validatedByUserId)) {
                    return false; // User already validated a step
                }
            }
        }
        return true;
    }
}
