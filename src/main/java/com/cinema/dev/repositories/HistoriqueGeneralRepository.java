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
    
    @Query(value = "SELECT * FROM historique_general h WHERE " +
           "(CAST(:nomTable AS VARCHAR) IS NULL OR h.nom_table = :nomTable) AND " +
           "(CAST(:idUtilisateur AS INTEGER) IS NULL OR h.id_utilisateur = :idUtilisateur) AND " +
           "(CAST(:startDate AS TIMESTAMP) IS NULL OR h.date_historique >= :startDate) AND " +
           "(CAST(:endDate AS TIMESTAMP) IS NULL OR h.date_historique <= :endDate) " +
           "ORDER BY h.date_historique DESC", nativeQuery = true)
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
