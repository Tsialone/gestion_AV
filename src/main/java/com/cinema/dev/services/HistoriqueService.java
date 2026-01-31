package com.cinema.dev.services;

import com.cinema.dev.models.HistoriqueGeneral;
import com.cinema.dev.models.Utilisateur;
import com.cinema.dev.repositories.HistoriqueGeneralRepository;
import com.cinema.dev.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoriqueService {
    
    @Autowired
    private HistoriqueGeneralRepository historiqueGeneralRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    /**
     * Get all historique entries, most recent first
     */
    public List<HistoriqueGeneral> findAll() {
        return historiqueGeneralRepository.findAllByOrderByDateHistoriqueDesc();
    }
    
    /**
     * Get historique entries with filters
     */
    public List<HistoriqueGeneral> findWithFilters(String nomTable, Integer idUtilisateur, 
                                                    LocalDateTime startDate, LocalDateTime endDate) {
        return historiqueGeneralRepository.findWithFilters(nomTable, idUtilisateur, startDate, endDate);
    }
    
    /**
     * Get historique by table name
     */
    public List<HistoriqueGeneral> findByTable(String nomTable) {
        return historiqueGeneralRepository.findByNomTableOrderByDateHistoriqueDesc(nomTable);
    }
    
    /**
     * Get historique by user
     */
    public List<HistoriqueGeneral> findByUser(Integer idUtilisateur) {
        return historiqueGeneralRepository.findByIdUtilisateurOrderByDateHistoriqueDesc(idUtilisateur);
    }
    
    /**
     * Get utilisateur name by ID (for display purposes)
     */
    public String getUtilisateurNom(Integer idUtilisateur) {
        return utilisateurRepository.findById(idUtilisateur)
            .map(Utilisateur::getNom)
            .orElse("Utilisateur inconnu");
    }
    
    /**
     * Get all unique table names from historique
     */
    public List<String> getDistinctTables() {
        return historiqueGeneralRepository.findAll().stream()
            .map(HistoriqueGeneral::getNomTable)
            .distinct()
            .sorted()
            .toList();
    }
}
