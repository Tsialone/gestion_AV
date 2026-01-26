package com.cinema.dev.controllers;

import com.cinema.dev.services.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/commande")
public class CommandeController {
    
    @Autowired
    private CommandeService commandeService;
    
    @GetMapping("/liste")
    public String getListe(Model model) {
        model.addAttribute("commandes", commandeService.findAll());
        model.addAttribute("content", "pages/commande/commande-liste");
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