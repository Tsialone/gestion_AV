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

import com.cinema.dev.models.JournalMvtStock;
import com.cinema.dev.repositories.JournalMvtStockRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/journal-mvt")
@RequiredArgsConstructor
public class JournalMvtStockController {

    private final JournalMvtStockRepository journalMvtStockRepository;

    @GetMapping("/liste")
    public String getListe(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            Model model) {
        
        LocalDateTime filterDate = date != null ? date : LocalDateTime.now();
        String dateFormatted = filterDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        
        List<JournalMvtStock> journalMvts = journalMvtStockRepository.findJournalFiltered(filterDate);
        
        // Calculer les totaux par type
        Integer totalEntrees = journalMvts.stream()
            .filter(j -> j.getEntrant())
            .mapToInt(JournalMvtStock::getQuantite)
            .sum();
        
        Integer totalSorties = journalMvts.stream()
            .filter(j -> !j.getEntrant())
            .mapToInt(JournalMvtStock::getQuantite)
            .sum();
        
        model.addAttribute("journalMvts", journalMvts);
        model.addAttribute("totalEntrees", totalEntrees);
        model.addAttribute("totalSorties", totalSorties);
        model.addAttribute("date", dateFormatted);
        model.addAttribute("content", "pages/journal-mvt/journal-mvt-liste");
        return "admin-layout";
    }
}
