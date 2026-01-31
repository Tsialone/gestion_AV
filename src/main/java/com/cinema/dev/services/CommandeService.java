package com.cinema.dev.services;

import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommandeService {
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private CommandeEtatRepository commandeEtatRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ProformaEtatRepository proformaEtatRepository;
    
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }
    
    public List<Commande> findWithFilters(Integer idProforma, LocalDateTime startDate, LocalDateTime endDate) {
        return commandeRepository.findWithFilters(idProforma, startDate, endDate);
    }

    @Transactional
    public Commande creerCommande(Integer idProforma, LocalDateTime dateCommande) {
        //* -- Get proforma
        Proforma proforma = proformaRepository.findById(idProforma)
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Use provided date or current date
        LocalDateTime dateCmd = dateCommande != null ? dateCommande : LocalDateTime.now();
        
        //? === RG: Validation ===
        //* -- Date proforma still valid (current < date_fin)
        if (dateCmd.isAfter(proforma.getDateFin())) {
            throw new IllegalArgumentException("Proforma expires at " + proforma.getDateFin() + ". Input: " + dateCommande);
        }
        
        //* -- Proforma etat is valid (etat = 2)
        if (!proformaEtatRepository.existsByIdProformaAndIdEtat(idProforma, 2)) {
            throw new IllegalArgumentException("Proforma is not validated");
        }
        
        //* -- Create commande
        Commande commande = new Commande();
        commande.setIdProforma(idProforma);
        commande.setDate(dateCmd);
        
        //* -- RG: Date commande after date proforma
        if (commande.getDate().isBefore(proforma.getDateDebut())) {
            throw new IllegalArgumentException("Commande date must be after proforma date");
        }
        
        //* -- Insert commande
        Commande savedCommande = commandeRepository.save(commande);
        
        //* -- Insert commande_etat (cree = 1)
        CommandeEtat commandeEtat = new CommandeEtat(savedCommande.getIdCommande(), 1, dateCmd);
        commandeEtatRepository.save(commandeEtat);
        
        return savedCommande;
    }
    
    @Transactional
    public CommandeEtat validerCommande(Integer idCommande, LocalDateTime dateValidation) {
        //* -- Check if commande exists
        commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        //* -- Insert commande_etat (valide = 2)
        LocalDateTime date = dateValidation != null ? dateValidation : LocalDateTime.now();
        CommandeEtat commandeEtat = new CommandeEtat(idCommande, 2, date);
        
        return commandeEtatRepository.save(commandeEtat);
    }
}