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

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MvtStockService {
    private final MvtStockLotRepository mvtStockLotRepository;
    private final MvtStockRepository mvtStockRepository;
    private final LotService lotService;
    private final MvtStockLotService mvtStockLotService;

    @Transactional(rollbackFor = Exception.class)
    public MvtStock creeerMvtStockSortie(MvtStockForm mvtStockForm) throws Exception {
        // List<Lot> lotsCree = lotService.creeLots(mvtStockForm.getArticleQte().size(), mvtStockForm.getArticleQte());
        MvtStock mvtStock = new MvtStock();

        mvtStock.setDate(mvtStockForm.getDate().atTime(0, 0));
        mvtStock.setDescriptionQualite(mvtStockForm.getDescriptionQualite());
        mvtStock.setDesignation(mvtStockForm.getDesignation());
        mvtStock.setEntrant(false);
        mvtStock.setIdDepot(mvtStockForm.getIdDepot());
        mvtStock.setIdLivraison(mvtStockForm.getIdLivraison());
        System.out.println(mvtStock.toString());
        System.out.println("xxxxxxxxxx ");
        MvtStock savedMvtStock = mvtStockRepository.save(mvtStock);
        // mvtStockLotService.creerListeMvtStockSortieLot(savedMvtStock.getIdMvt(), lotsCree);

        for (Integer idArticle : mvtStockForm.getArticleQte().keySet()) {
            Integer qte = mvtStockForm.getArticleQte().get(idArticle);
            mvtStockLotService.faireSortirDesProduitsLeLotImportePeu(idArticle, qte, savedMvtStock.getIdMvt());
        }

        return savedMvtStock;
    }

    @Transactional(rollbackFor = Exception.class)
    public MvtStock creerMvtStockEntree(MvtStockForm mvtStockForm) throws Exception {
        List<Lot> lotsCree = lotService.creeLots(mvtStockForm.getArticleQte().size(), mvtStockForm.getArticleQte());
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
