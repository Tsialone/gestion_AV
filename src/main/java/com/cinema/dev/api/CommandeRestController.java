package com.cinema.dev.api;

import com.cinema.dev.services.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/commande")
public class CommandeRestController {
    
    @Autowired
    private PaiementService paiementService;
    
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
}
