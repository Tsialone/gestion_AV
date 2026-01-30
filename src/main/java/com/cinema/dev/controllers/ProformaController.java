package com.cinema.dev.controllers;

import com.cinema.dev.models.Proforma;
import com.cinema.dev.models.ProformaDetail;
import com.cinema.dev.services.ProformaService;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.repositories.DemandeAchatRepository;
import com.cinema.dev.repositories.ProformaEtatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Controller
@RequestMapping("/proforma")
public class ProformaController {
    
    @Autowired
    private ProformaService proformaService;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private DemandeAchatRepository demandeAchatRepository;
    
    @Autowired
    private ProformaEtatRepository proformaEtatRepository;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) Integer idClient, @RequestParam(required = false) Integer idFournisseur, 
                           @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, Model model) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        model.addAttribute("proformas", proformaService.findWithFilters(idClient, idFournisseur, start, end));
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("proformaEtats", proformaEtatRepository.findAll());
        model.addAttribute("filterIdClient", idClient);
        model.addAttribute("filterIdFournisseur", idFournisseur);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        model.addAttribute("content", "pages/proforma/proforma-liste");
        return "admin-layout";
    }
    
    @GetMapping("/creer-client")
    public String getCreerClient(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("demandesAchat", demandeAchatRepository.findAll());
        model.addAttribute("content", "pages/proforma/creer-proforma-client");
        return "admin-layout";
    }
    
    @GetMapping("/creer-fournisseur")
    public String getCreerFournisseur(Model model) {
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("demandesAchat", demandeAchatRepository.findAll());
        model.addAttribute("content", "pages/proforma/creer-proforma-fournisseur");
        return "admin-layout";
    }
    
    @GetMapping("/saisie")
    public String getSaisie(Model model) {
        model.addAttribute("content", "pages/proforma/proforma-saisie");
        return "admin-layout";
    }
    
    @PostMapping("/creer")
    public String creerProforma(
            @RequestParam Integer idDemandeAchat,
            @RequestParam(required = false) Integer idClient,
            @RequestParam(required = false) Integer idFournisseur,
            @RequestParam(required = false) LocalDateTime dateCreation,
            @ModelAttribute Proforma proforma,
            @RequestParam Integer[] idArticles,
            @RequestParam BigDecimal[] prix,
            @RequestParam Integer[] quantites) {
        
        ProformaDetail[] details = new ProformaDetail[idArticles.length];
        for (int i = 0; i < idArticles.length; i++) {
            ProformaDetail detail = new ProformaDetail();
            ProformaDetail.ProformaDetailId id = new ProformaDetail.ProformaDetailId();
            id.setIdArticle(idArticles[i]);
            detail.setId(id);
            detail.setPrix(prix[i]);
            detail.setQuantite(quantites[i]);
            details[i] = detail;
        }
        
        proformaService.creerProforma(idDemandeAchat, idClient, idFournisseur, proforma, details, dateCreation);
        return "redirect:/proforma/liste";
    }
    
    @PostMapping("/valider/{idProforma}")
    public String validerProforma(
            @PathVariable Integer idProforma,
            @RequestParam(required = false) LocalDateTime dateValidation) {
        proformaService.validerProforma(idProforma, dateValidation);
        return "redirect:/proforma/liste";
    }
}