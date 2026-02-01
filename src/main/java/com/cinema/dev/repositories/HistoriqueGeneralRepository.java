package com.cinema.dev.repositories;

import com.cinema.dev.models.HistoriqueGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueGeneralRepository extends JpaRepository<HistoriqueGeneral, Integer> {
    
    @Query("SELECT h FROM HistoriqueGeneral h WHERE " +
           "(:nomTable IS NULL OR h.nomTable = :nomTable) AND " +
           "(:idUtilisateur IS NULL OR h.idUtilisateur = :idUtilisateur) AND " +
           "(:startDate IS NULL OR h.dateHistorique >= :startDate) AND " +
           "(:endDate IS NULL OR h.dateHistorique <= :endDate) " +
           "ORDER BY h.dateHistorique DESC")
    List<HistoriqueGeneral> findWithFilters(
        @Param("nomTable") String nomTable,
        @Param("idUtilisateur") Integer idUtilisateur,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    List<HistoriqueGeneral> findByNomTableOrderByDateHistoriqueDesc(String nomTable);
    
    List<HistoriqueGeneral> findByIdUtilisateurOrderByDateHistoriqueDesc(Integer idUtilisateur);
    
    List<HistoriqueGeneral> findAllByOrderByDateHistoriqueDesc();
}
