package com.cinema.dev.repositories;

import com.cinema.dev.models.SortieStockLot;
import com.cinema.dev.models.SortieStockLotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SortieStockLotRepository extends JpaRepository<SortieStockLot, SortieStockLotId> {

    // Recherche par ID de mouvement
    List<SortieStockLot> findByIdMvt(Integer idMvt);

    // Récupérer toutes les sorties triées par date décroissante
    List<SortieStockLot> findAllByOrderByDateMouvementDesc();
}