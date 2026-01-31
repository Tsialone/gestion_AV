package com.cinema.dev.controllers;

import com.cinema.dev.models.Livraison;
import com.cinema.dev.models.Commande;
import com.cinema.dev.models.Proforma;
import com.cinema.dev.repositories.LivraisonRepository;
import com.cinema.dev.repositories.CommandeRepository;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.utils.BreadcrumbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/livraison")
public class LivraisonController {
    
    @Autowired
    private LivraisonRepository livraisonRepository;
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) Integer idCommande,
                          @RequestParam(required = false) String type,
                          @RequestParam(required = false) String startDate,
                          @RequestParam(required = false) String endDate,
                          @RequestParam(required = false, defaultValue = "idLivraison") String sortBy,
                          @RequestParam(required = false, defaultValue = "desc") String sortDir,
                          Model model) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        // Get all livraisons
        List<Livraison> allLivraisons = livraisonRepository.findAll();
        
        // Apply filters
        List<Livraison> filteredLivraisons = allLivraisons.stream()
            .filter(l -> {
                // Filter by commande
                if (idCommande != null && !l.getIdCommande().equals(idCommande)) {
                    return false;
                }
                
                // Filter by date range
                if (start != null && l.getDate().isBefore(start)) {
                    return false;
                }
                if (end != null && l.getDate().isAfter(end)) {
                    return false;
                }
                
                // Filter by type (client/fournisseur)
                if (type != null && !type.isEmpty()) {
                    Commande cmd = commandeRepository.findById(l.getIdCommande()).orElse(null);
                    if (cmd == null || cmd.getIdProforma() == null) {
                        return false;
                    }
                    
                    Proforma proforma = proformaRepository.findById(cmd.getIdProforma()).orElse(null);
                    if (proforma == null) {
                        return false;
                    }
                    
                    if ("client".equals(type)) {
                        return proforma.getIdClient() != null;
                    } else if ("fournisseur".equals(type)) {
                        return proforma.getIdFournisseur() != null;
                    }
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        // Build maps for type and partenaire names
        Map<Integer, String> livraisonTypes = new HashMap<>();
        Map<Integer, String> partenaireNames = new HashMap<>();
        Map<Integer, Integer> commandeIds = new HashMap<>();
        
        for (Livraison l : filteredLivraisons) {
            commandeIds.put(l.getIdLivraison(), l.getIdCommande());
            
            Commande cmd = commandeRepository.findById(l.getIdCommande()).orElse(null);
            if (cmd != null && cmd.getIdProforma() != null) {
                Proforma proforma = proformaRepository.findById(cmd.getIdProforma()).orElse(null);
                if (proforma != null) {
                    if (proforma.getIdClient() != null) {
                        livraisonTypes.put(l.getIdLivraison(), "client");
                        clientRepository.findById(proforma.getIdClient()).ifPresent(client -> 
                            partenaireNames.put(l.getIdLivraison(), client.getNom())
                        );
                    } else if (proforma.getIdFournisseur() != null) {
                        livraisonTypes.put(l.getIdLivraison(), "fournisseur");
                        fournisseurRepository.findById(proforma.getIdFournisseur()).ifPresent(fournisseur -> 
                            partenaireNames.put(l.getIdLivraison(), fournisseur.getNom())
                        );
                    }
                }
            }
        }
        
        // Apply sorting
        java.util.Comparator<Livraison> comparator = null;
        switch (sortBy) {
            case "idLivraison":
                comparator = java.util.Comparator.comparing(Livraison::getIdLivraison);
                break;
            case "date":
                comparator = java.util.Comparator.comparing(Livraison::getDate, java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
                break;
            case "idCommande":
                comparator = java.util.Comparator.comparing(Livraison::getIdCommande, java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
                break;
        }
        
        if (comparator != null) {
            if ("desc".equals(sortDir)) {
                comparator = comparator.reversed();
            }
            filteredLivraisons.sort(comparator);
        }
        
        model.addAttribute("livraisons", filteredLivraisons);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("commandes", commandeRepository.findAll());
        model.addAttribute("livraisonTypes", livraisonTypes);
        model.addAttribute("partenaireNames", partenaireNames);
        model.addAttribute("commandeIds", commandeIds);
        model.addAttribute("filterIdCommande", idCommande);
        model.addAttribute("filterType", type);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Livraisons");
        model.addAttribute("pageSubtitle", "Gestion des livraisons");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Livraisons", "/livraison/liste")
        ));
        
        model.addAttribute("content", "pages/livraison/livraison-liste");
        return "admin-layout";
    }
}