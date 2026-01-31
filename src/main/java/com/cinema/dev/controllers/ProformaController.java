package com.cinema.dev.controllers;

import com.cinema.dev.models.Proforma;
import com.cinema.dev.models.ProformaDetail;
import com.cinema.dev.services.ProformaService;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.repositories.DemandeAchatRepository;
import com.cinema.dev.repositories.ProformaEtatRepository;
import com.cinema.dev.utils.BreadcrumbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
                           @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
                           @RequestParam(required = false, defaultValue = "idProforma") String sortBy,
                           @RequestParam(required = false, defaultValue = "desc") String sortDir,
                           Model model) {
        
        LocalDateTime start = (startDate != null && !startDate.isEmpty()) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty()) ? LocalDateTime.parse(endDate) : null;
        
        List<Proforma> proformas = proformaService.findWithFilters(idClient, idFournisseur, start, end);
        
        // Apply sorting
        java.util.Comparator<Proforma> comparator = null;
        switch (sortBy) {
            case "idProforma":
                comparator = java.util.Comparator.comparing(Proforma::getIdProforma);
                break;
            case "dateDebut":
                comparator = java.util.Comparator.comparing(Proforma::getDateDebut, java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
                break;
            case "dateFin":
                comparator = java.util.Comparator.comparing(Proforma::getDateFin, java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder()));
                break;
        }
        
        if (comparator != null) {
            if ("desc".equals(sortDir)) {
                comparator = comparator.reversed();
            }
            proformas.sort(comparator);
        }
        
        model.addAttribute("proformas", proformas);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("proformaEtats", proformaEtatRepository.findAll());
        model.addAttribute("filterIdClient", idClient);
        model.addAttribute("filterIdFournisseur", idFournisseur);
        model.addAttribute("filterStartDate", startDate);
        model.addAttribute("filterEndDate", endDate);
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Proformas");
        model.addAttribute("pageSubtitle", "Gestion des proformas");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Proformas", "/proforma/liste")
        ));
        
        model.addAttribute("content", "pages/proforma/proforma-liste");
        return "admin-layout";
    }
    
    @GetMapping("/creer-client")
    public String getCreerClient(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("demandesAchat", demandeAchatRepository.findAll().stream()
            .filter(da -> da.getIdClient() != null)
            .toList());
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Nouveau Proforma");
        model.addAttribute("pageSubtitle", "Créer un proforma pour un client");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Proformas", "/proforma/liste"),
            new BreadcrumbItem("Nouveau (Client)", "/proforma/creer-client")
        ));
        
        model.addAttribute("content", "pages/proforma/creer-proforma-client");
        return "admin-layout";
    }
    
    @GetMapping("/creer-fournisseur")
    public String getCreerFournisseur(Model model) {
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("demandesAchat", demandeAchatRepository.findAll().stream()
            .filter(da -> da.getIdClient() == null)
            .toList());
        
        // Page title and breadcrumbs
        model.addAttribute("pageTitle", "Nouveau Proforma");
        model.addAttribute("pageSubtitle", "Créer un proforma pour un fournisseur");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Proformas", "/proforma/liste"),
            new BreadcrumbItem("Nouveau (Fournisseur)", "/proforma/creer-fournisseur")
        ));
        
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
            @RequestParam Integer[] quantites,
            RedirectAttributes redirectAttributes) {
        
        try {
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
            redirectAttributes.addFlashAttribute("toastMessage", "Proforma créé avec succès");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return idClient != null ? "redirect:/proforma/creer-client" : "redirect:/proforma/creer-fournisseur";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Une erreur est survenue: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return idClient != null ? "redirect:/proforma/creer-client" : "redirect:/proforma/creer-fournisseur";
        }
        return "redirect:/proforma/liste";
    }
    
    @PostMapping("/valider/{idProforma}")
    public String validerProforma(
            @PathVariable Integer idProforma,
            @RequestParam(required = false) LocalDateTime dateValidation,
            RedirectAttributes redirectAttributes) {
        try {
            proformaService.validerProforma(idProforma, dateValidation);
            redirectAttributes.addFlashAttribute("toastMessage", "Proforma validé avec succès");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Une erreur est survenue: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
        return "redirect:/proforma/liste";
    }
}