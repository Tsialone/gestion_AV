package com.cinema.dev.controllers;

import com.cinema.dev.models.HistoriqueGeneral;
import com.cinema.dev.services.AuthorizationService;
import com.cinema.dev.services.HistoriqueService;
import com.cinema.dev.utils.BreadcrumbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

@Controller
@RequestMapping("/historique")
public class HistoriqueController {
    
    @Autowired
    private HistoriqueService historiqueService;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) String nomTable,
                          @RequestParam(required = false) Integer idUtilisateur,
                          @RequestParam(required = false) String startDate,
                          @RequestParam(required = false) String endDate,
                          @RequestParam(required = false, defaultValue = "dateHistorique") String sortBy,
                          @RequestParam(required = false, defaultValue = "desc") String sortDir,
                          Model model) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        List<HistoriqueGeneral> historiques = historiqueService.findWithFilters(nomTable, idUtilisateur, start, end);
        
        // Apply sorting
        Comparator<HistoriqueGeneral> comparator = null;
        switch (sortBy) {
            case "idHg":
                comparator = Comparator.comparing(HistoriqueGeneral::getIdHg);
                break;
            case "dateHistorique":
                comparator = Comparator.comparing(HistoriqueGeneral::getDateHistorique, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "nomTable":
                comparator = Comparator.comparing(HistoriqueGeneral::getNomTable, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
        }
        
        if (comparator != null) {
            if ("desc".equals(sortDir)) {
                comparator = comparator.reversed();
            }
            historiques.sort(comparator);
        }
        
        // Create a map of user IDs to names for display
        Map<Integer, String> utilisateurNames = authorizationService.findAllUtilisateurs().stream()
            .collect(Collectors.toMap(u -> u.getIdUtilisateur(), u -> u.getNom()));
        
        model.addAttribute("historiques", historiques);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("utilisateurs", authorizationService.findAllUtilisateurs());
        model.addAttribute("utilisateurNames", utilisateurNames);
        model.addAttribute("tables", historiqueService.getDistinctTables());
        model.addAttribute("filterNomTable", nomTable);
        model.addAttribute("filterIdUtilisateur", idUtilisateur);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Historique Général");
        model.addAttribute("pageSubtitle", "Journal des actions effectuées");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Historique", "/historique/liste")
        ));
        
        model.addAttribute("content", "pages/historique/historique-liste");
        return "admin-layout";
    }
}
