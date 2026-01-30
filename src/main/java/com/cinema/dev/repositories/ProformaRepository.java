package com.cinema.dev.repositories;

import com.cinema.dev.models.Proforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProformaRepository extends JpaRepository<Proforma, Integer> {
    
    @Query("SELECT p FROM Proforma p WHERE " +
        "CASE WHEN :idClient IS NULL THEN true ELSE p.idClient = :idClient END " +
        "AND CASE WHEN :idFournisseur IS NULL THEN true ELSE p.idFournisseur = :idFournisseur END " +
        "AND CASE WHEN CAST(:startDate AS timestamp) IS NULL THEN true ELSE p.dateDebut >= :startDate END " +
        "AND CASE WHEN CAST(:endDate AS timestamp) IS NULL THEN true ELSE p.dateFin <= :endDate END " +
        "ORDER BY p.dateDebut DESC")
    List<Proforma> findWithFilters(
        @Param("idClient") Integer idClient,
        @Param("idFournisseur") Integer idFournisseur,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}