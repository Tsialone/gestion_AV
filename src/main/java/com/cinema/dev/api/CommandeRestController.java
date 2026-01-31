package com.cinema.dev.api;

import com.cinema.dev.models.Commande;
import com.cinema.dev.models.Proforma;
import com.cinema.dev.repositories.CommandeRepository;
import com.cinema.dev.repositories.ProformaDetailRepository;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.repositories.ClientRepository;
import com.cinema.dev.repositories.FournisseurRepository;
import com.cinema.dev.services.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            BigDecimal reste = paiementService.getMontantTotalPourUneCommande(idCommande, LocalDateTime.now());
            
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
}
