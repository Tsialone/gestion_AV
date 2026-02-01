package com.cinema.dev.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cinema.dev.dtos.ArticleCategorieStatDto;
import com.cinema.dev.dtos.BestClientDto;
import com.cinema.dev.dtos.BestFournisseurDto;
import com.cinema.dev.dtos.EtatStockDto;
import com.cinema.dev.dtos.EtatStockRecapDto;
import com.cinema.dev.dtos.VenteEvolutionDto;
import com.cinema.dev.models.Depot;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.repositories.DepotRepository;
import com.cinema.dev.repositories.EtatStockRepository;
import com.cinema.dev.repositories.ProformaDetailRepository;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.services.MvtCaisseService;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class DashboardController {

    private final MvtCaisseService mvtCaisseService;

    private final ProformaDetailRepository proformaDetailRepository;
    private final EtatStockRepository etatStockRepository;
    private final ArticleRepository articleRepository;
    private final DepotRepository depotRepository;

    @GetMapping("/")
    public String goHome(Model model) {

        Double caVente = mvtCaisseService.getTotalCredit();
        Double depense = mvtCaisseService.getTotalDebit();

        Integer articleVendue = proformaDetailRepository.sumTotalArticlesVendus();

        Integer articleStock = 0;
        List<EtatStockDto> etatStockDtos = etatStockRepository.findEtatStockFiltered(null, null, LocalDateTime.now());
        List<EtatStockRecapDto> etatStockRecapDtos = new ArrayList<>();
        List<Depot> depots = depotRepository.findAll();
        for (EtatStockDto dto : etatStockDtos) {
            articleStock += dto.getStockADate();

        }

        for (Depot depot : depots) {
            int total = 0;
            for (EtatStockDto dto : etatStockDtos) {
                if (depot.getIdDepot().equals(dto.getIdDepot())) {
                    total += dto.getStockADate();
                    
                }

            }
            Double pourcentage =  (double)total / depot.getCapacite() * 100;
            System.out.println("total: " + total);
            System.out.println("capacite: " + depot.getCapacite());
            System.out.println("pourcentage: " + pourcentage);
            etatStockRecapDtos
                    .add(new EtatStockRecapDto(depot.getNom(), pourcentage));
        }
        List<ArticleCategorieStatDto> articleCategorieStatDtos = articleRepository.findNbrArticlesVendusByCategorie();
        List<VenteEvolutionDto> venteEvolutionDtos = proformaDetailRepository.findVentesEvolution();

        List<BestFournisseurDto> bestFournisseurs = proformaDetailRepository.findBestFournisseurs(3);
        List<BestClientDto> bestClients = proformaDetailRepository.findBestClients(3);

        System.out.println(venteEvolutionDtos);

        model.addAttribute("caVente", caVente);
        model.addAttribute("depense", depense);
        model.addAttribute("articleVendue", articleVendue);
        model.addAttribute("bestFournisseurs", bestFournisseurs);
        model.addAttribute("bestClients", bestClients);

        model.addAttribute("articleStock", articleStock);
        model.addAttribute("etatStockRecap", etatStockRecapDtos);
        model.addAttribute("venteEvolution", venteEvolutionDtos);
        model.addAttribute("articleCategorieStats", articleCategorieStatDtos);

        model.addAttribute("content", "pages/dashboard-stats");

        return "admin-layout";
    }
}