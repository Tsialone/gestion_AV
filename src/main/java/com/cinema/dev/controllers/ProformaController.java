package com.cinema.dev.controllers;

import com.cinema.dev.models.Proforma;
import com.cinema.dev.models.ProformaDetail;
import com.cinema.dev.services.ProformaService;
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
    
    @GetMapping("/liste")
    public String getListe(Model model) {
        model.addAttribute("proformas", proformaService.findAll());
        model.addAttribute("content", "pages/proforma/proforma-liste");
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