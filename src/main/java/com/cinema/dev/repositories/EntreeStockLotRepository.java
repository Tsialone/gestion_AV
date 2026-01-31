package com.cinema.dev.repositories;

import com.cinema.dev.models.EntreeStockLot;
import com.cinema.dev.models.EntreeStockLotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntreeStockLotRepository extends JpaRepository<EntreeStockLot, EntreeStockLotId> {

    // Recherche par ID de mouvement
    List<EntreeStockLot> findByIdMvt(Integer idMvt);

    // Récupérer toutes les entrées triées par date décroissante (déjà géré par la vue, mais sécurisé ici)
    List<EntreeStockLot> findAllByOrderByDateMouvementDesc();
}