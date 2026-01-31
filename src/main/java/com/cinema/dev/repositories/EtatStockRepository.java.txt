package com.cinema.dev.repositories;

import com.cinema.dev.dtos.EtatStockDto;
import com.cinema.dev.models.EtatStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EtatStockRepository extends JpaRepository<EtatStock, Long> {

    @Query("""
                SELECT new com.cinema.dev.dtos.EtatStockDto(
                    e.idDepot,
                    e.depot,
                    e.idArticle,
                    e.article,
                    CAST(SUM(e.entree) AS INTEGER) AS total_entrees,
                    CAST(SUM(e.sortie) AS INTEGER) AS total_sorties,
                    CAST(SUM(e.variation) AS INTEGER) AS stock_a_date
                )
                FROM EtatStock e
                WHERE (:idDepot IS NULL OR e.idDepot = :idDepot)
                  AND e.dateMouvement <= COALESCE(:date, e.dateMouvement)
                GROUP BY e.idDepot, e.depot, e.idArticle, e.article
                ORDER BY e.depot, e.article
            """)
    List<EtatStockDto> findEtatStockFiltered(
            @Param("idDepot") Long idDepot,
            @Param("date") LocalDateTime date);

}
