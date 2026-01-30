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
    public String getListe(Model model) {
        model.addAttribute("demandesAchat", demandeAchatService.findAll());
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
        
        demandeAchatService.effectuerDemandeAchat(idClient, demandeAchat, details);
        return "redirect:/demande-achat/liste";
    }
}