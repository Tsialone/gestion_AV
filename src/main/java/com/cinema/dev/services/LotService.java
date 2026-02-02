package com.cinema.dev.services;

import com.cinema.dev.models.Article;
import com.cinema.dev.models.Lot;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.repositories.LotRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LotService {
    private final LotRepository lotRepository;
    private final ArticleRepository articleRepository;
    
    // private final MvtStockLotService mvtStockLotService;
    

    public List<Lot>  getLotsByArticleWithPositiveStock(Integer idArticle) {
        List<Lot> lots = lotRepository.findByIdArticle(idArticle);
        List<Lot> filteredLots = new ArrayList<>();
        for (Lot lot : lots) {
            if (lot.getQte() != null && lot.getQte() > 0
            ) {
                filteredLots.add(lot);
            }
        }
        return filteredLots;
    }
    
   

    public Integer getQttRestantDansLotApresSortie(Integer idLot, Integer qteSortie) throws Exception {
        Lot lot = lotRepository.findById(idLot).orElseThrow(() -> new Exception("Lot non trouvé: " + idLot));
        Integer qteRestant = lot.getQte() - qteSortie;
        return qteRestant >= 0 ? qteRestant : 0;
    }

    public List<Lot> creeLots(Integer nombre, HashMap<Integer, Integer> articleQt) throws Exception {
    List<Lot> resp = new ArrayList<>();

    // On boucle sur chaque entrée (ArticleID -> Quantité) de la Map
    for (Map.Entry<Integer, Integer> entry : articleQt.entrySet()) {
        Integer idArticle = entry.getKey();
        Integer qte = entry.getValue();

        System.out.println("Traitement : idArticle = " + idArticle + " | qte = " + qte);

        Article article = articleRepository.findById(idArticle)
                .orElseThrow(() -> new Exception("Article non trouvé: " + idArticle));

        if (qte <= 0) {
            throw new Exception("Quantité invalide pour l'article: " + idArticle);
        }

        Lot lot = new Lot();
        lot.setIdArticle(idArticle);
        lot.setQte(qte);
        lot.setQteInitiale(qte);
        // On utilise le libellé de l'article pour le lot
        lot.setLibelle(article.getLibelle() + " - Lot automatique");
        
        resp.add(lotRepository.save(lot));
    }

    return resp;
}

    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }

    public Optional<Lot> getLotById(Integer id) {
        return lotRepository.findById(id);
    }

    public List<Lot> getLotsByArticle(Integer idArticle) {
        return lotRepository.findByIdArticle(idArticle);
    }

    public Lot createLot(Lot lot) {
        return lotRepository.save(lot);
    }

    public Lot updateLot(Integer id, Lot lot) {
        if (lotRepository.existsById(id)) {
            lot.setIdLot(id);
            return lotRepository.save(lot);
        }
        return null;
    }

    public void deleteLot(Integer id) {
        lotRepository.deleteById(id);
    }
}
