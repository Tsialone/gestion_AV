package com.cinema.dev.controllers;

import com.cinema.dev.models.DemandeAchat;
import com.cinema.dev.models.DemandeAchatDetail;
import com.cinema.dev.services.DemandeAchatService;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.utils.BreadcrumbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/demande-achat")
public class DemandeAchatController {
    
    @Autowired
    private DemandeAchatService demandeAchatService;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) Integer idClient, @RequestParam(required = false) String startDate, 
                          @RequestParam(required = false) String endDate, Model model) {
        
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;
        
        model.addAttribute("demandesAchat", demandeAchatService.findWithFilters(idClient, start, end));
        model.addAttribute("clients", clientRepository.findAll());
        Map<Integer, String> clientsMap = clientRepository.findAll()
            .stream().collect(Collectors.toMap(c -> c.getIdClient(), c -> c.getNom()));
        model.addAttribute("clientsMap", clientsMap);
        model.addAttribute("filterIdClient", idClient);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Demandes d'Achat");
        model.addAttribute("pageSubtitle", "Gestion des demandes d'achat clients et fournisseurs");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Demandes d'Achat", "/demande-achat/liste")
        ));
        
        model.addAttribute("content", "pages/demande-achat/demande-achat-liste");
        return "admin-layout";
    }
    
    @GetMapping("/saisie-client")
    public String getSaisieClient(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Nouvelle Demande d'Achat");
        model.addAttribute("pageSubtitle", "Créer une nouvelle demande d'achat depuis un client");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Demandes d'Achat", "/demande-achat/liste"),
            new BreadcrumbItem("Nouvelle (Client)", "/demande-achat/saisie-client")
        ));
        
        model.addAttribute("content", "pages/demande-achat/demande-achat-client");
        return "admin-layout";
    }
    
    @GetMapping("/saisie-fournisseur")
    public String getSaisieFournisseur(Model model) {
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Nouvelle Demande d'Achat");
        model.addAttribute("pageSubtitle", "Créer une nouvelle demande d'achat depuis un fournisseur");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Demandes d'Achat", "/demande-achat/liste"),
            new BreadcrumbItem("Nouvelle (Fournisseur)", "/demande-achat/saisie-fournisseur")
        ));
        
        model.addAttribute("content", "pages/demande-achat/demande-achat-fournisseur");
        return "admin-layout";
    }
    
    @GetMapping("/saisie")
    public String getSaisie(Model model) {
        model.addAttribute("content", "pages/demande-achat/demande-achat-saisie");
        return "admin-layout";
    }
    
    @PostMapping("/effectuer")
        public String effectuerDemandeAchat(
            @RequestParam(required = false) Integer idClient,
            @RequestParam(required = false) Integer idFournisseur,
            @ModelAttribute DemandeAchat demandeAchat,
            @RequestParam Integer[] idArticles,
            @RequestParam Integer[] quantites) {
        
        DemandeAchatDetail[] details = new DemandeAchatDetail[idArticles.length];
        for (int i = 0; i < idArticles.length; i++) {
            DemandeAchatDetail detail = new DemandeAchatDetail();
            DemandeAchatDetail.DemandeAchatDetailId id = new DemandeAchatDetail.DemandeAchatDetailId();
            id.setIdArticle(idArticles[i]);
            detail.setId(id);
            detail.setQuantite(quantites[i]);
            details[i] = detail;
        }
        
        if (demandeAchat.getDateDemande() == null) {
            demandeAchat.setDateDemande(LocalDate.now());
        }

        // normalize: if idClient is null but idFournisseur provided, store fournisseur id in the same field
        Integer partyId = (idClient != null) ? idClient : idFournisseur;
        demandeAchatService.effectuerDemandeAchat(partyId, demandeAchat, details);
        return "redirect:/demande-achat/liste";
    }
}