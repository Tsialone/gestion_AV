package com.cinema.dev.repositories;

import com.cinema.dev.models.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    
    @Query("SELECT c FROM Commande c WHERE " +
        "CASE WHEN :idProforma IS NULL THEN true ELSE c.idProforma = :idProforma END " +
        "AND CASE WHEN CAST(:startDate AS timestamp) IS NULL THEN true ELSE c.date >= :startDate END " +
        "AND CASE WHEN CAST(:endDate AS timestamp) IS NULL THEN true ELSE c.date <= :endDate END " +
        "ORDER BY c.date DESC")
    List<Commande> findWithFilters(
        @Param("idProforma") Integer idProforma,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}