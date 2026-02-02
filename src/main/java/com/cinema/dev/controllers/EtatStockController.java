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

import com.cinema.dev.dtos.EtatStockDto;
import com.cinema.dev.repositories.DepotRepository;
import com.cinema.dev.repositories.EtatStockRepository;
import com.cinema.dev.services.AuthorizationService;
import com.cinema.dev.services.SessionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/etat-stock")
@RequiredArgsConstructor
public class EtatStockController {

    private final EtatStockRepository etatStockRepository;

    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private DepotRepository depotRepository;

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
                "Accès refusé. Seul le département Logistique ou Direction peut accéder à l'état du stock.");
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
            @RequestParam(value = "idDepot", required = false) Integer idDepot,
            @RequestParam(value = "idArticle", required = false) Integer idArticle,
            Model model) {
        String accessCheck = checkLogistiqueAccess(session, redirectAttributes);
        if (accessCheck != null) return accessCheck;
        
        LocalDateTime filterDate = date != null ? date : LocalDateTime.now();
        String dateFormatted = filterDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        
        List<EtatStockDto> etatStocks = etatStockRepository.findEtatStockFiltered(idDepot, idArticle, filterDate);
        
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
        model.addAttribute("idArticle", idArticle);
        model.addAttribute("depots", this.depotRepository.findAll());
        model.addAttribute("content", "pages/etat-stock/etat-stock-liste");
        return "admin-layout";
    }
}
