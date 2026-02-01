package com.cinema.dev.api;

import com.cinema.dev.models.DemandeAchat;
import com.cinema.dev.repositories.DemandeAchatRepository;
import com.cinema.dev.repositories.DemandeAchatDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/demande-achat")
public class DemandeAchatRestController {
    
    @Autowired
    private DemandeAchatRepository demandeAchatRepository;
    
    @Autowired
    private DemandeAchatDetailRepository demandeAchatDetailRepository;
    
    @GetMapping("/{idDa}")
    public Map<String, Object> getDemandeAchatDetail(@PathVariable Integer idDa) {
        Map<String, Object> response = new HashMap<>();
        Optional<DemandeAchat> demande = demandeAchatRepository.findById(idDa);
        
        if (demande.isPresent()) {
            response.put("idDa", demande.get().getIdDa());
            response.put("idClient", demande.get().getIdClient());
            response.put("dateDemande", demande.get().getDateDemande());
            
            // Fetch details with article names
            List<Map<String, Object>> details = demandeAchatDetailRepository.findDetailsWithArticleNames(idDa);
            response.put("details", details);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "Demande d'achat non trouvée");
        }
        
        return response;
    }
}
