package com.cinema.dev.services;

import com.cinema.dev.forms.MvtStockForm;
import com.cinema.dev.models.Article;
import com.cinema.dev.models.Lot;
import com.cinema.dev.models.MvtStock;
import com.cinema.dev.models.MvtStockLot;
import com.cinema.dev.models.MvtStockLot.MvtStockLotId;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.repositories.LotRepository;
import com.cinema.dev.repositories.MvtStockLotRepository;
import com.cinema.dev.repositories.MvtStockRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MvtStockService {
    private final MvtStockLotRepository mvtStockLotRepository;
    private final MvtStockRepository mvtStockRepository;
    private final LotService lotService;
    private final MvtStockLotService mvtStockLotService;

    @Transactional
    public MvtStock creerMvtStockEntree(MvtStockForm mvtStockForm) throws Exception {
        List<Lot> lotsCree = lotService.creeLots(1, mvtStockForm.getArticleQte());
        MvtStock mvtStock = new MvtStock();

        mvtStock.setDate(mvtStockForm.getDate().atTime(0, 0));
        mvtStock.setDescriptionQualite(mvtStockForm.getDescriptionQualite());
        mvtStock.setDesignation(mvtStockForm.getDesignation());
        mvtStock.setEntrant(mvtStockForm.isEntrant());
        mvtStock.setIdDepot(mvtStockForm.getIdDepot());
        mvtStock.setIdLivraison(mvtStockForm.getIdLivraison());

        MvtStock savedMvtStock = mvtStockRepository.save(mvtStock);
        mvtStockLotService.creerListeMvtStockEntreeLot(savedMvtStock.getIdMvt(), lotsCree);

        return savedMvtStock;
    }

    public List<MvtStockLot> getAll() {
        return mvtStockLotRepository.findAll();
    }

}
