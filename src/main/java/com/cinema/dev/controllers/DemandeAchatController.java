package com.cinema.dev.controllers;

import com.cinema.dev.models.DemandeAchat;
import com.cinema.dev.models.DemandeAchatDetail;
import com.cinema.dev.services.DemandeAchatService;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

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
        model.addAttribute("filterIdClient", idClient);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        model.addAttribute("content", "pages/demande-achat/demande-achat-liste");
        return "admin-layout";
    }
    
    @GetMapping("/saisie-client")
    public String getSaisieClient(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("content", "pages/demande-achat/demande-achat-client");
        return "admin-layout";
    }
    
    @GetMapping("/saisie-fournisseur")
    public String getSaisieFournisseur(Model model) {
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
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

        demandeAchatService.effectuerDemandeAchat(idClient, demandeAchat, details);
        return "redirect:/demande-achat/liste";
    }
}