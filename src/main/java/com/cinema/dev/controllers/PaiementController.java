package com.cinema.dev.controllers;

import com.cinema.dev.models.Paiement;
import com.cinema.dev.models.Commande;
import com.cinema.dev.models.Proforma;
import com.cinema.dev.services.PaiementService;
import com.cinema.dev.utils.BreadcrumbItem;
import com.cinema.dev.repositories.CommandeRepository;
import com.cinema.dev.repositories.CaisseRepository;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/paiement")
public class PaiementController {
    
    @Autowired
    private PaiementService paiementService;
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private CaisseRepository caisseRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) Integer idCommande,
                          @RequestParam(required = false) String type,
                          @RequestParam(required = false) String startDate,
                          @RequestParam(required = false) String endDate,
                          Model model) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        List<Paiement> paiements = paiementService.findWithFilters(idCommande, type, start, end);
        
        // Build maps for type and partenaire names
        Map<Integer, String> paiementTypes = new HashMap<>();
        Map<Integer, String> partenaireNames = new HashMap<>();
        
        for (Paiement p : paiements) {
            Commande cmd = commandeRepository.findById(p.getIdCommande()).orElse(null);
            if (cmd != null && cmd.getIdProforma() != null) {
                Proforma proforma = proformaRepository.findById(cmd.getIdProforma()).orElse(null);
                if (proforma != null) {
                    if (proforma.getIdClient() != null) {
                        paiementTypes.put(p.getIdPaiement(), "client");
                        clientRepository.findById(proforma.getIdClient()).ifPresent(client -> 
                            partenaireNames.put(p.getIdPaiement(), client.getNom())
                        );
                    } else if (proforma.getIdFournisseur() != null) {
                        paiementTypes.put(p.getIdPaiement(), "fournisseur");
                        fournisseurRepository.findById(proforma.getIdFournisseur()).ifPresent(fournisseur -> 
                            partenaireNames.put(p.getIdPaiement(), fournisseur.getNom())
                        );
                    }
                }
            }
        }
        
        model.addAttribute("paiements", paiements);
        model.addAttribute("commandes", commandeRepository.findAll());
        model.addAttribute("caisses", caisseRepository.findAll());
        model.addAttribute("paiementTypes", paiementTypes);
        model.addAttribute("partenaireNames", partenaireNames);
        model.addAttribute("filterIdCommande", idCommande);
        model.addAttribute("filterType", type);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        model.addAttribute("content", "pages/paiement/paiement-liste");

        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Gestion Paiements");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Paiement", "/paiement/liste")
        ));

        return "admin-layout";
    }
    
    @GetMapping("/saisie")
    public String getSaisie(Model model) {
        model.addAttribute("commandes", commandeRepository.findAll());
        model.addAttribute("caisses", caisseRepository.findAll());
        model.addAttribute("content", "pages/commande/payement-commande");

        model.addAttribute("pageTitle", "Paiements Saisie");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Paiement", "/paiement/saisie")
        ));

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