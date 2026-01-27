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

import jakarta.transaction.Transactional;
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
    private final LotRepository lotRepository;

    private final LotService lotService;

    public Integer faireSortirMaxDansUnLotEtRetournerReste(Integer idLot, Integer nombreMax, Integer idMvtStock)
            throws Exception {
        Integer nombreASortir = nombreMax;
        Lot lot = lotRepository.findById(idLot)
                .orElseThrow(() -> new Exception("Lot non trouvé: " + idLot));
        if (nombreASortir > lot.getQte()) {
            nombreASortir = lot.getQte();
        }
        faireSortirDansLot(idLot, nombreASortir, idMvtStock);
        return nombreMax - nombreASortir;
    }

    @Transactional(rollbackOn = Exception.class)
    public void faireSortirDesProduitsLeLotImportePeu(Integer idArticle, Integer nombre,
            Integer idMvtStock) throws Exception {
        List<Lot> lots = lotService.getLotsByArticleWithPositiveStock(idArticle);
        // List<MvtStockLot> resp = new ArrayList<>();

        Integer reste = nombre;
        for (Lot lot : lots) {
            if (reste <= 0) {
                break;
            }
            Integer resteASortir = faireSortirMaxDansUnLotEtRetournerReste(lot.getIdLot(), reste,
                    idMvtStock);
            if (resteASortir != 0) {
                throw new Exception("il manque " + resteASortir + " dans le stock");
            } else {
                return;
            }
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public MvtStockLot faireSortirDansLot(Integer idLot, Integer nombre, Integer idMvtStock) throws Exception {
        Lot lot = lotRepository.findById(idLot)
                .orElseThrow(() -> new Exception("Lot non trouvé: " + idLot));
        Integer qteRestant = lotService.getQttRestantDansLotApresSortie(idLot, nombre);
        if (qteRestant <= 0) {
            throw new Exception("Quantité insuffisante dans le lot: " + idLot);
        }
        lot.setQte(qteRestant);
        lotRepository.save(lot);
        MvtStock mvtStock = mvtStockRepository.findById(idMvtStock)
                .orElseThrow(() -> new Exception("MvtStock non trouvé: " + idMvtStock));

        if (mvtStock.getEntrant()) {
            throw new Exception("Le mouvement de stock doit être une sortie pour faire sortir des articles d'un lot: "
                    + idMvtStock);
        }

        MvtStockLot mvtStockLot = new MvtStockLot();
        mvtStockLot.setQte(nombre);
        MvtStockLotId mvtStockLotId = new MvtStockLotId();
        mvtStockLotId.setIdLot(idLot);
        mvtStockLotId.setIdMvt(idMvtStock);

        mvtStockLot.setIdMvtStockLot(mvtStockLotId);

        return mvtStockLotRepository.save(mvtStockLot);
    }

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
