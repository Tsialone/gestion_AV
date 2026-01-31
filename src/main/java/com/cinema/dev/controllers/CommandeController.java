package com.cinema.dev.controllers;

import com.cinema.dev.services.CommandeService;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.models.Proforma;
import com.cinema.dev.repositories.CaisseRepository;
import com.cinema.dev.repositories.CommandeEtatRepository;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.ProformaEtatRepository;
import com.cinema.dev.repositories.LivraisonRepository;
import com.cinema.dev.utils.BreadcrumbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/commande")
public class CommandeController {
    
    @Autowired
    private CommandeService commandeService;
    
    @Autowired
    private ProformaRepository proformaRepository;

    @Autowired
    private ProformaEtatRepository proformaEtatRepository;
    
    @Autowired
    private CaisseRepository caisseRepository;
    
    @Autowired
    private CommandeEtatRepository commandeEtatRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @Autowired
    private LivraisonRepository livraisonRepository;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) Integer idProforma, @RequestParam(required = false) Integer idClient, @RequestParam(required = false) Integer idFournisseur, @RequestParam(required = false) String startDate, 
                           @RequestParam(required = false) String endDate, Model model) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        var commandes = commandeService.findWithFilters(idProforma, start, end);
        model.addAttribute("commandes", commandes);
        
        // Create a map of commande ID to livraison for easy lookup in template
        Map<Integer, Boolean> commandeLivraisons = commandes.stream()
            .collect(Collectors.toMap(
                c -> c.getIdCommande(),
                c -> livraisonRepository.findByIdCommande(c.getIdCommande()).isPresent()
            ));
        model.addAttribute("commandeLivraisons", commandeLivraisons);
        
        model.addAttribute("proformas", proformaRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("caisses", caisseRepository.findAll());
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
        List<Proforma> validatedP = proformaRepository.findAll().stream()
            .filter(p -> proformaEtatRepository.existsByIdProformaAndIdEtat(p.getIdProforma(), 2) 
                      && !proformaEtatRepository.existsByIdProformaAndIdEtat(p.getIdProforma(), 3))
            .toList();

        model.addAttribute("proformas", validatedP);

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
    public String creerCommande(@PathVariable Integer idProforma, @RequestParam(required = false) LocalDateTime dateCommande,
                                RedirectAttributes redirectAttributes) {
        try {
            commandeService.creerCommande(idProforma, dateCommande);
            redirectAttributes.addFlashAttribute("toastMessage", "Commande créée avec succès");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Une erreur est survenue: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/commande/liste";
    }
    
    @PostMapping("/valider/{idCommande}")
    public String validerCommande(@PathVariable Integer idCommande, @RequestParam(required = false) LocalDateTime dateValidation,
                                  RedirectAttributes redirectAttributes) {
        try {
            commandeService.validerCommande(idCommande, dateValidation);
            redirectAttributes.addFlashAttribute("toastMessage", "Commande validée avec succès");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Une erreur est survenue: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/commande/liste";
    }
    
    @PostMapping("/livrer/{idCommande}")
    public String livrerCommande(@PathVariable Integer idCommande, @RequestParam(required = false) LocalDateTime dateLivraison,
                                 RedirectAttributes redirectAttributes) {
        try {
            commandeService.livrerCommande(idCommande, dateLivraison);
            redirectAttributes.addFlashAttribute("toastMessage", "Commande livrée avec succès");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Une erreur est survenue: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/commande/liste";
    }
}