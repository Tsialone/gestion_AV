package com.cinema.dev.controllers;

import com.cinema.dev.services.CommandeService;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.repositories.CaisseRepository;
import com.cinema.dev.repositories.CommandeEtatRepository;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.utils.BreadcrumbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;

@Controller
@RequestMapping("/commande")
public class CommandeController {
    
    @Autowired
    private CommandeService commandeService;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private CaisseRepository caisseRepository;
    
    @Autowired
    private CommandeEtatRepository commandeEtatRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) Integer idProforma, @RequestParam(required = false) Integer idClient, @RequestParam(required = false) Integer idFournisseur, @RequestParam(required = false) String startDate, 
                           @RequestParam(required = false) String endDate, Model model) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        model.addAttribute("commandes", commandeService.findWithFilters(idProforma, start, end));
        model.addAttribute("proformas", proformaRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("commandeEtats", commandeEtatRepository.findAll());
        model.addAttribute("filterIdProforma", idProforma);
        model.addAttribute("filterIdClient", idClient);
        model.addAttribute("filterIdFournisseur", idFournisseur);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Commandes");
        model.addAttribute("pageSubtitle", "Gestion des commandes");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Commandes", "/commande/liste")
        ));
        
        model.addAttribute("content", "pages/commande/commande-liste");
        return "admin-layout";
    }
    
    @GetMapping("/creer")
    public String getCreer(Model model) {
        model.addAttribute("proformas", proformaRepository.findAll());
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Nouvelle Commande");
        model.addAttribute("pageSubtitle", "Créer une nouvelle commande");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Commandes", "/commande/liste"),
            new BreadcrumbItem("Nouvelle", "/commande/creer")
        ));
        
        model.addAttribute("content", "pages/commande/creation-commande");
        return "admin-layout";
    }
    
    @GetMapping("/paiement")
    public String getPaiement(Model model) {
        model.addAttribute("commandes", commandeService.findAll());
        model.addAttribute("caisses", caisseRepository.findAll());
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Paiement Commandes");
        model.addAttribute("pageSubtitle", "Gérer les paiements des commandes");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Commandes", "/commande/liste"),
            new BreadcrumbItem("Paiement", "/commande/paiement")
        ));
        
        model.addAttribute("content", "pages/commande/payement-commande");
        return "admin-layout";
    }
    
    @PostMapping("/creer/{idProforma}")
    public String creerCommande(@PathVariable Integer idProforma, @RequestParam(required = false) LocalDateTime dateCommande) {
        commandeService.creerCommande(idProforma, dateCommande);
        return "redirect:/commande/liste";
    }
    
    @PostMapping("/valider/{idCommande}")
    public String validerCommande(@PathVariable Integer idCommande, @RequestParam(required = false) LocalDateTime dateValidation) {
        commandeService.validerCommande(idCommande, dateValidation);
        return "redirect:/commande/liste";
    }
}