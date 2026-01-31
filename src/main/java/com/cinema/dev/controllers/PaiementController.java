package com.cinema.dev.controllers;

import com.cinema.dev.models.Paiement;
import com.cinema.dev.services.PaiementService;
import com.cinema.dev.repositories.CommandeRepository;
import com.cinema.dev.repositories.CaisseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/paiement")
public class PaiementController {
    
    @Autowired
    private PaiementService paiementService;
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private CaisseRepository caisseRepository;
    
    @GetMapping("/liste")
    public String getListe(Model model) {
        model.addAttribute("paiements", paiementService.findAll());
        model.addAttribute("content", "pages/paiement/paiement-liste");
        return "admin-layout";
    }
    
    @GetMapping("/saisie")
    public String getSaisie(Model model) {
        model.addAttribute("commandes", commandeRepository.findAll());
        model.addAttribute("caisses", caisseRepository.findAll());
        model.addAttribute("content", "pages/commande/payement-commande");
        return "admin-layout";
    }
    
    @PostMapping("/payer")
    public String payerCommande(@RequestParam Integer idCommande, @RequestParam Integer idCaisse, 
                                @RequestParam(required = false) LocalDateTime dateMvtCaisse, @ModelAttribute Paiement paiement,
                                RedirectAttributes redirectAttributes) {
        try {
            paiementService.payerCommande(idCommande, idCaisse, paiement, dateMvtCaisse);
            redirectAttributes.addFlashAttribute("toastMessage", "Paiement enregistré avec succès");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Une erreur est survenue: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/paiement/liste";
    }
}