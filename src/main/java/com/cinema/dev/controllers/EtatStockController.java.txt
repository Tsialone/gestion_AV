package com.cinema.dev.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cinema.dev.dtos.EtatStockDto;
import com.cinema.dev.repositories.EtatStockRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/etat-stock")
@RequiredArgsConstructor
public class EtatStockController {

    private final EtatStockRepository etatStockRepository;

    @GetMapping("/liste")
    public String getListe(
            @RequestParam(value = "date", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(value = "idDepot", required = false) Long idDepot,
            Model model) {
        LocalDateTime filterDate = date != null ? date : LocalDateTime.now();
        String dateFormatted = filterDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        
        List<EtatStockDto> etatStocks = etatStockRepository.findEtatStockFiltered(idDepot, filterDate);
        
        // Calculer les totaux
        Integer totalEntrees = etatStocks.stream()
            .mapToInt(EtatStockDto::getTotalEntrees)
            .sum();
        
        Integer totalSorties = etatStocks.stream()
            .mapToInt(EtatStockDto::getTotalSorties)
            .sum();
        
        Integer stockActuel = etatStocks.stream()
            .mapToInt(EtatStockDto::getStockADate)
            .sum();
        
        model.addAttribute("etatStocks", etatStocks);
        model.addAttribute("totalEntrees", totalEntrees);
        model.addAttribute("totalSorties", totalSorties);
        model.addAttribute("stockActuel", stockActuel);
        model.addAttribute("date", dateFormatted);
        model.addAttribute("idDepot", idDepot);
        model.addAttribute("content", "pages/etat-stock/etat-stock-liste");
        return "admin-layout";
    }
}
