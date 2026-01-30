package com.cinema.dev.controllers;

import com.cinema.dev.models.Paiement;
import com.cinema.dev.services.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/paiement")
public class PaiementController {
    
    @Autowired
    private PaiementService paiementService;
    
    @GetMapping("/liste")
    public String getListe(Model model) {
        model.addAttribute("paiements", paiementService.findAll());
        model.addAttribute("content", "pages/paiement/paiement-liste");
        return "admin-layout";
    }
    
    @GetMapping("/saisie")
    public String getSaisie(Model model) {
        model.addAttribute("content", "pages/paiement/paiement-saisie");
        return "admin-layout";
    }
    
    @PostMapping("/payer")
    public String payerCommande(
            @RequestParam Integer idCommande,
            @RequestParam Integer idCaisse,
            @RequestParam(required = false) LocalDateTime dateMvtCaisse,
            @ModelAttribute Paiement paiement) {
        
        paiementService.payerCommande(idCommande, idCaisse, paiement, dateMvtCaisse);
        return "redirect:/paiement/liste";
    }
}