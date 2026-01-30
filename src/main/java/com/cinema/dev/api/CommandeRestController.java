package com.cinema.dev.api;

import com.cinema.dev.models.Commande;
import com.cinema.dev.models.ProformaDetail;
import com.cinema.dev.repositories.CommandeRepository;
import com.cinema.dev.repositories.ProformaDetailRepository;
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
            
            // Fetch proforma details using the commande's idProforma
            Integer idProforma = commande.get().getIdProforma();
            if (idProforma != null) {
                List<ProformaDetail> details = proformaDetailRepository.findByIdProforma(idProforma);
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
