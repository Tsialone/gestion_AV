package com.cinema.dev.services;

import com.cinema.dev.models.Article;
import com.cinema.dev.models.Lot;
import com.cinema.dev.models.MvtStock;
import com.cinema.dev.models.MvtStockLot;
import com.cinema.dev.models.MvtStockLot.MvtStockLotId;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.repositories.LotRepository;
import com.cinema.dev.repositories.MvtStockLotRepository;
import com.cinema.dev.repositories.MvtStockRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MvtStockLotService {
    private final MvtStockLotRepository mvtStockLotRepository;
    private final MvtStockRepository mvtStockRepository;

    public List<MvtStockLot> creerListeMvtStockEntreeLot(Integer idMvtStock, List<Lot> listeLots) throws Exception {
        List<MvtStockLot> resp = new ArrayList<>();
        mvtStockRepository.findById(idMvtStock)
                .orElseThrow(() -> new Exception("Mvststock non trouver " + idMvtStock));
        for (Lot lot : listeLots) {
            MvtStockLot mvtStockLot = new MvtStockLot();
            mvtStockLot.setQte(lot.getQte());
            MvtStockLotId mvtStockLotId = new MvtStockLotId();
            mvtStockLotId.setIdLot(lot.getIdLot());
            mvtStockLotId.setIdMvt(idMvtStock);

            mvtStockLot.setIdMvtStockLot(mvtStockLotId);

            resp.add(mvtStockLotRepository.save(mvtStockLot));
        }

        return resp;
    }

    public List<MvtStockLot> getAll() {
        return mvtStockLotRepository.findAll();
    }

}
