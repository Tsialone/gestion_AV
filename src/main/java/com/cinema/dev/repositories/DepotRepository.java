package com.cinema.dev.repositories;

import com.cinema.dev.models.Depot;
import com.cinema.dev.models.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Integer> {
    
    @Query(value = """
        SELECT DISTINCT l.id_lot
        FROM mvt_stock ms
        JOIN mvt_stock_lot msl ON msl.id_mvt = ms.id_mvt
        JOIN lot l ON l.id_lot = msl.id_lot
        JOIN article a ON a.id_article = l.id_article
        JOIN categorie c ON c.id_categorie = a.id_categorie
        WHERE ms.id_depot = :idDepot
    """, nativeQuery = true)
    List<Integer> findLotsIdsByIdDepot(@Param("idDepot") Integer idDepot);
}
