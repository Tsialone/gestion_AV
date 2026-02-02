package com.cinema.dev.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cinema.dev.models.JournalMvtStock;
import com.cinema.dev.repositories.JournalMvtStockRepository;
import com.cinema.dev.services.AuthorizationService;
import com.cinema.dev.services.SessionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/journal-mvt")
@RequiredArgsConstructor
public class JournalMvtStockController {

    private final JournalMvtStockRepository journalMvtStockRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private SessionService sessionService;

    /**
     * Check if current user has access to this module (Logistique or Direction only)
     */
    private String checkLogistiqueAccess(HttpSession session, RedirectAttributes redirectAttributes) {
        Integer userId = sessionService.getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        if (!authorizationService.isInLogistiqueOrDirection(userId)) {
            redirectAttributes.addFlashAttribute("toastMessage", 
                "Accès refusé. Seul le département Logistique ou Direction peut accéder au journal des mouvements.");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/";
        }
        return null; // Access granted
    }

    @GetMapping("/liste")
    public String getListe(
            HttpSession session, RedirectAttributes redirectAttributes,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            Model model) {
        String accessCheck = checkLogistiqueAccess(session, redirectAttributes);
        if (accessCheck != null) return accessCheck;
        
        LocalDateTime filterDate = date != null ? date : LocalDateTime.now();
        String dateFormatted = filterDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        
        List<JournalMvtStock> journalMvts = journalMvtStockRepository.findJournalFiltered(filterDate);
        for(int i  = 0; i < journalMvts.size(); i ++) {
            System.out.println("lot = " + journalMvts.get(i).getLot() + " artcile = " + journalMvts.get(i).getArticle());
        }

        // // Calculer les totaux par type
        // Integer totalEntrees = journalMvts.stream()
        //     .filter(j -> j.getEntrant())
        //     .mapToInt(JournalMvtStock::getQuantite)
        //     .sum();
        
        // Integer totalSorties = journalMvts.stream()
        //     .filter(j -> !j.getEntrant())
        //     .mapToInt(JournalMvtStock::getQuantite)
        //     .sum();
        
        model.addAttribute("journalMvts", journalMvts);
        // model.addAttribute("totalEntrees", totalEntrees);
        // model.addAttribute("totalSorties", totalSorties);
          model.addAttribute("totalEntrees", 0);
        // model.addAttribute("totalSorties", totalSorties);
        model.addAttribute("date", 0);
        model.addAttribute("content", "pages/journal-mvt/journal-mvt-liste");
        return "admin-layout";
    }
}
