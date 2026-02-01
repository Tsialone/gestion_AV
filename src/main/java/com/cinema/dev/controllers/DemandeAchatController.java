package com.cinema.dev.controllers;

import com.cinema.dev.models.DemandeAchat;
import com.cinema.dev.models.DemandeAchatDetail;
import com.cinema.dev.services.DemandeAchatService;
import com.cinema.dev.services.SessionService;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.ArticleRepository;
import com.cinema.dev.utils.BreadcrumbItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

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
    
    @Autowired
    private SessionService sessionService;
    
    @GetMapping("/liste")
    public String getListe(@RequestParam(required = false) Integer idClient, @RequestParam(required = false) String startDate, 
                          @RequestParam(required = false) String endDate,
                          @RequestParam(required = false, defaultValue = "idDa") String sortBy,
                          @RequestParam(required = false, defaultValue = "desc") String sortDir,
                          Model model) {
        
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;
        
        List<DemandeAchat> demandesAchat = demandeAchatService.findWithFilters(idClient, start, end);
        
        // Apply sorting
        Comparator<DemandeAchat> comparator = null;
        switch (sortBy) {
            case "idDa":
                comparator = Comparator.comparing(DemandeAchat::getIdDa);
                break;
            case "dateDemande":
                comparator = Comparator.comparing(DemandeAchat::getDateDemande, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "idClient":
                comparator = Comparator.comparing(DemandeAchat::getIdClient, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
        }
        
        if (comparator != null) {
            if ("desc".equals(sortDir)) {
                comparator = comparator.reversed();
            }
            demandesAchat.sort(comparator);
        }
        
        model.addAttribute("demandesAchat", demandesAchat);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
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
            HttpSession session,
            @RequestParam(required = false) Integer idClient,
            @RequestParam(required = false) Integer idFournisseur,
            @ModelAttribute DemandeAchat demandeAchat,
            @RequestParam Integer[] idArticles,
            @RequestParam Integer[] quantites,
            RedirectAttributes redirectAttributes) {
        
        Integer idUtilisateur = sessionService.getCurrentUserId(session);
        
        try {
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

            // For fournisseur demandes, idClient should remain null
            // Only pass idClient when it's actually a client demande
            demandeAchatService.effectuerDemandeAchat(idUtilisateur, idClient, demandeAchat, details);
            redirectAttributes.addFlashAttribute("toastMessage", "Demande d'achat créée avec succès");
            redirectAttributes.addFlashAttribute("toastType", "success");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return idClient != null ? "redirect:/demande-achat/saisie-client" : "redirect:/demande-achat/saisie-fournisseur";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return idClient != null ? "redirect:/demande-achat/saisie-client" : "redirect:/demande-achat/saisie-fournisseur";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Une erreur est survenue: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return idClient != null ? "redirect:/demande-achat/saisie-client" : "redirect:/demande-achat/saisie-fournisseur";
        }
        return "redirect:/demande-achat/liste";
    }
}