package com.cinema.dev.api;

import com.cinema.dev.models.Proforma;
import com.cinema.dev.repositories.ProformaRepository;
import com.cinema.dev.repositories.ProformaDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/proforma")
public class ProformaRestController {
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ProformaDetailRepository proformaDetailRepository;
    
    @GetMapping("/{idProforma}")
    public Map<String, Object> getProformaDetail(@PathVariable Integer idProforma) {
        Map<String, Object> response = new HashMap<>();
        Optional<Proforma> proforma = proformaRepository.findById(idProforma);
        
        if (proforma.isPresent()) {
            response.put("idProforma", proforma.get().getIdProforma());
            response.put("dateDebut", proforma.get().getDateDebut());
            response.put("dateFin", proforma.get().getDateFin());
            response.put("idClient", proforma.get().getIdClient());
            response.put("idFournisseur", proforma.get().getIdFournisseur());
            
            // Fetch details with article names
            List<Map<String, Object>> details = proformaDetailRepository.findDetailsWithArticleNames(idProforma);
            response.put("details", details);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "Proforma non trouvé");
        }
        
        return response;
    }
}
