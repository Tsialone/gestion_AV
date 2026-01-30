package com.cinema.dev.services;

import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class PaiementService {
    
    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private MvtCaisseRepository mvtCaisseRepository;
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Transactional
    public Paiement payerCommande(Integer idCommande, Integer idCaisse, Paiement paiement, LocalDateTime dateMvtCaisse) {
        //* -- Get commande and proforma to check client/fournisseur
        Commande commande = commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        Proforma proforma = proformaRepository.findById(commande.getIdProforma())
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Set idCommande
        paiement.setIdCommande(idCommande);
        
        //* -- Insert paiement
        Paiement savedPaiement = paiementRepository.save(paiement);
        
        //* -- Create mvt_caisse
        MvtCaisse mvtCaisse = new MvtCaisse();
        mvtCaisse.setIdPaiement(savedPaiement.getIdPaiement());
        mvtCaisse.setIdCaisse(idCaisse);
        mvtCaisse.setDate(dateMvtCaisse != null ? dateMvtCaisse : LocalDateTime.now());
        
        //* -- Debit if idFournisseur != null, Credit if idClient != null
        if (proforma.getIdFournisseur() != null) {
            mvtCaisse.setDebit(paiement.getMontant());
            mvtCaisse.setCredit(BigDecimal.ZERO);
        } else if (proforma.getIdClient() != null) {
            mvtCaisse.setDebit(BigDecimal.ZERO);
            mvtCaisse.setCredit(paiement.getMontant());
        }
        
        //* -- Insert mvt_caisse
        mvtCaisseRepository.save(mvtCaisse);
        
        return savedPaiement;
    }
}