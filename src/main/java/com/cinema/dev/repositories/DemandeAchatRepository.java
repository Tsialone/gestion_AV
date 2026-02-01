package com.cinema.dev.repositories;

import com.cinema.dev.models.DemandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandeAchatRepository extends JpaRepository<DemandeAchat, Integer> {
    
    @Query("SELECT d FROM DemandeAchat d WHERE " +
           "CASE WHEN :idClient IS NULL THEN true ELSE d.idClient = :idClient END " +
           "AND CASE WHEN :startDate IS NULL THEN true ELSE d.dateDemande >= :startDate END " +
           "AND CASE WHEN :endDate IS NULL THEN true ELSE d.dateDemande <= :endDate END " +
           "ORDER BY d.dateDemande DESC")
    List<DemandeAchat> findWithFilters(
        @Param("idClient") Integer idClient,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}