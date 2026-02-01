package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "validation_step")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_validation_step")
    private Integer idValidationStep;
    
    @Column(name = "entity_type", nullable = false, length = 20)
    private String entityType;  // "proforma" or "commande"
    
    @Column(name = "entity_id", nullable = false)
    private Integer entityId;
    
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;  // 1 or 2
    
    @Column(name = "id_utilisateur", nullable = false)
    private Integer idUtilisateur;
    
    @Column(name = "validated_at", nullable = false)
    private LocalDateTime validatedAt;
    
    // Convenience constructor without id
    public ValidationStep(String entityType, Integer entityId, Integer stepNumber, 
                          Integer idUtilisateur, LocalDateTime validatedAt) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.stepNumber = stepNumber;
        this.idUtilisateur = idUtilisateur;
        this.validatedAt = validatedAt;
    }
}
