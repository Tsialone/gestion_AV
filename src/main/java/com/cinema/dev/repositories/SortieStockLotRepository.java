package com.cinema.dev.repositories;

import com.cinema.dev.models.SortieStockLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SortieStockLotRepository extends JpaRepository<SortieStockLot, Integer> {

    // Recherche par ID de mouvement
    List<SortieStockLot> findByIdMvt(Integer idMvt);

    // Récupérer toutes les sorties triées par date décroissante
    List<SortieStockLot> findAllByOrderByDateMouvementDesc();
}