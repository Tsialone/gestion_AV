package com.cinema.dev.repositories;

import com.cinema.dev.models.ValidationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ValidationStepRepository extends JpaRepository<ValidationStep, Integer> {
    
    /**
     * Find all validation steps for an entity
     */
    List<ValidationStep> findByEntityTypeAndEntityIdOrderByStepNumber(String entityType, Integer entityId);
    
    /**
     * Find a specific step for an entity
     */
    Optional<ValidationStep> findByEntityTypeAndEntityIdAndStepNumber(String entityType, Integer entityId, Integer stepNumber);
    
    /**
     * Check if a step exists for an entity
     */
    boolean existsByEntityTypeAndEntityIdAndStepNumber(String entityType, Integer entityId, Integer stepNumber);
    
    /**
     * Check if a user has already validated any step for this entity
     */
    boolean existsByEntityTypeAndEntityIdAndIdUtilisateur(String entityType, Integer entityId, Integer idUtilisateur);
    
    /**
     * Get the highest step number completed for an entity
     */
    @Query("SELECT MAX(vs.stepNumber) FROM ValidationStep vs WHERE vs.entityType = :entityType AND vs.entityId = :entityId")
    Optional<Integer> findMaxStepNumberByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Integer entityId);
    
    /**
     * Count validation steps for an entity
     */
    int countByEntityTypeAndEntityId(String entityType, Integer entityId);
}
