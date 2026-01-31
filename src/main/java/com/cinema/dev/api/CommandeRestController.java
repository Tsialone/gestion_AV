package com.cinema.dev.api;

import com.cinema.dev.models.Commande;
import com.cinema.dev.models.Proforma;
import com.cinema.dev.models.Livraison;
import com.cinema.dev.repositories.CommandeRepository;
import com.cinema.dev.repositories.ProformaDetailRepository;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.repositories.LivraisonRepository;
import com.cinema.dev.services.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/commande")
public class CommandeRestController {
    
    @Autowired
    private PaiementService paiementService;

    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private ProformaDetailRepository proformaDetailRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @Autowired
    private LivraisonRepository livraisonRepository;
    
    @GetMapping("/proforma/{idProforma}")
    public ResponseEntity<Map<String, Object>> getProformaDetails(@PathVariable Integer idProforma) {
        try {
            Proforma proforma = proformaRepository.findById(idProforma)
                .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("idProforma", proforma.getIdProforma());
            response.put("dateDebut", proforma.getDateDebut());
            response.put("dateFin", proforma.getDateFin());
            response.put("idClient", proforma.getIdClient());
            response.put("idFournisseur", proforma.getIdFournisseur());
            
            // Fetch client or fournisseur name
            if (proforma.getIdClient() != null) {
                clientRepository.findById(proforma.getIdClient()).ifPresent(client -> {
                    response.put("clientName", client.getNom());
                });
            } else if (proforma.getIdFournisseur() != null) {
                fournisseurRepository.findById(proforma.getIdFournisseur()).ifPresent(fournisseur -> {
                    response.put("fournisseurName", fournisseur.getNom());
                });
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/{idCommande}/reste")
    public ResponseEntity<Map<String, Object>> getResteCommande(@PathVariable Integer idCommande) {
        try {
            BigDecimal reste = paiementService.getMontantTotalPourUneCommande(idCommande, null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("idCommande", idCommande);
            response.put("reste", reste);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{idCommande}/paiement-info")
    public ResponseEntity<Map<String, Object>> getPaiementInfo(@PathVariable Integer idCommande) {
        try {
            Optional<Commande> commandeOpt = commandeRepository.findById(idCommande);
            if (commandeOpt.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Commande not found");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Commande commande = commandeOpt.get();
            Integer idProforma = commande.getIdProforma();
            
            // Get total amount from proforma
            BigDecimal total = BigDecimal.ZERO;
            if (idProforma != null) {
                total = paiementService.getTotalProforma(idProforma);
            }
            
            // Get amount already paid
            BigDecimal paye = paiementService.getSommePaiements(idCommande);
            
            // Get remaining amount
            BigDecimal reste = paiementService.getMontantTotalPourUneCommande(idCommande, null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", total);
            response.put("paye", paye);
            response.put("reste", reste);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{idCommande}")
    public Map<String, Object> getCommandeDetail(@PathVariable Integer idCommande) {
        Map<String, Object> response = new HashMap<>();
        Optional<Commande> commande = commandeRepository.findById(idCommande);
        
        if (commande.isPresent()) {
            response.put("idCommande", commande.get().getIdCommande());
            response.put("date", commande.get().getDate());
            response.put("idProforma", commande.get().getIdProforma());
            
            // Fetch proforma details with article names using the commande's idProforma
            Integer idProforma = commande.get().getIdProforma();
            if (idProforma != null) {
                List<Map<String, Object>> details = proformaDetailRepository.findDetailsWithArticleNames(idProforma);
                response.put("details", details);
            } else {
                response.put("details", List.of());
            }
            
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "Commande non trouvée");
        }
        
        return response;
    }

    @GetMapping("/proforma-by-commande/{idCommande}")
    public ResponseEntity<Map<String, Object>> getProformaByCommande(@PathVariable Integer idCommande) {
        try {
            Optional<Commande> commandeOpt = commandeRepository.findById(idCommande);
            if (commandeOpt.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Commande not found");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Commande commande = commandeOpt.get();
            Integer idProforma = commande.getIdProforma();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("idProforma", idProforma);
            response.put("date", commande.getDate());
            
            if (idProforma != null) {
                Optional<Proforma> proformaOpt = proformaRepository.findById(idProforma);
                if (proformaOpt.isPresent()) {
                    Proforma proforma = proformaOpt.get();
                    
                    // Set client or fournisseur name
                    if (proforma.getIdClient() != null) {
                        clientRepository.findById(proforma.getIdClient()).ifPresent(client -> {
                            response.put("clientName", client.getNom());
                        });
                    } else if (proforma.getIdFournisseur() != null) {
                        fournisseurRepository.findById(proforma.getIdFournisseur()).ifPresent(fournisseur -> {
                            response.put("fournisseurName", fournisseur.getNom());
                        });
                    }
                }
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/{idCommande}/livraison-info")
    public ResponseEntity<Map<String, Object>> getLivraisonInfo(@PathVariable Integer idCommande) {
        try {
            // Find livraison by idCommande
            List<Livraison> livraisons = livraisonRepository.findAll().stream()
                .filter(l -> l.getIdCommande().equals(idCommande))
                .toList();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            
            if (!livraisons.isEmpty()) {
                Livraison livraison = livraisons.get(0);
                Map<String, Object> livraisonData = new HashMap<>();
                livraisonData.put("idLivraison", livraison.getIdLivraison());
                livraisonData.put("date", livraison.getDate());
                livraisonData.put("idCommande", livraison.getIdCommande());
                response.put("livraison", livraisonData);
            } else {
                response.put("livraison", null);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}