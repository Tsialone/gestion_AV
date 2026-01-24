package com.cinema.dev.services;

import com.cinema.dev.models.Article;
import com.cinema.dev.models.Lot;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.repositories.LotRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LotService {
    private final LotRepository lotRepository;
    private final ArticleRepository articleRepository;



    
    public List<Lot> creeLots(Integer nombre, HashMap<Integer, Integer> articleQt) throws Exception {

        List<Lot> resp = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            Lot lot = new Lot();
            Integer idArticle = articleQt.keySet().iterator().next();
            Integer qte = articleQt.get(idArticle);
            Article article =   articleRepository.findById(idArticle).orElseThrow(() -> new Exception("Article non trouvé: " + idArticle));
            if (qte <= 0) {
                throw new Exception("Quantité invalide pour l'article: " + idArticle);
            }

            lot.setIdArticle(idArticle);
            lot.setQte(qte);
            lot.setLibelle(article.getLibelle() + " - Lot " + (i + 1));
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
