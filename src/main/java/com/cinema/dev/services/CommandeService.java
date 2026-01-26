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
            throw new IllegalArgumentException("Proforma has expired");
        }
        
        //* -- Proforma etat is valid
        ProformaEtat.ProformaEtatId etatId = new ProformaEtat.ProformaEtatId();
        etatId.setIdProforma(idProforma);
        etatId.setIdEtat(2); // "valide"
        if (!proformaEtatRepository.existsById(etatId)) {
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
        
        //* -- Insert commande_etat
        CommandeEtat commandeEtat = new CommandeEtat();
        CommandeEtat.CommandeEtatId cmdEtatId = new CommandeEtat.CommandeEtatId();
        cmdEtatId.setIdCommande(savedCommande.getIdCommande());
        cmdEtatId.setIdEtat(1); // "cree"
        commandeEtat.setId(cmdEtatId);
        commandeEtat.setDate(dateCmd);
        commandeEtatRepository.save(commandeEtat);
        
        return savedCommande;
    }
    
    @Transactional
    public CommandeEtat validerCommande(Integer idCommande, LocalDateTime dateValidation) {
        //* -- Insert commande_etat (valide)
        CommandeEtat commandeEtat = new CommandeEtat();
        CommandeEtat.CommandeEtatId etatId = new CommandeEtat.CommandeEtatId();
        etatId.setIdCommande(idCommande);
        etatId.setIdEtat(2); // "valide"
        commandeEtat.setId(etatId);
        commandeEtat.setDate(dateValidation != null ? dateValidation : LocalDateTime.now());
        
        return commandeEtatRepository.save(commandeEtat);
    }
}