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
    
    @Autowired
    private ProformaDetailRepository proformaDetailRepository;
    
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }
    
    public List<Paiement> findWithFilters(Integer idCommande, String type, LocalDateTime startDate, LocalDateTime endDate) {
        List<Paiement> allPaiements = paiementRepository.findWithFilters(idCommande, startDate, endDate);
        
        if (type == null || type.isEmpty()) {
            return allPaiements;
        }
        
        // Filter by type (client or fournisseur)
        return allPaiements.stream()
            .filter(p -> {
                Commande cmd = commandeRepository.findById(p.getIdCommande()).orElse(null);
                if (cmd == null || cmd.getIdProforma() == null) return false;
                
                Proforma proforma = proformaRepository.findById(cmd.getIdProforma()).orElse(null);
                if (proforma == null) return false;
                
                if ("client".equals(type)) {
                    return proforma.getIdClient() != null;
                } else if ("fournisseur".equals(type)) {
                    return proforma.getIdFournisseur() != null;
                }
                return true;
            })
            .toList();
    }

    @Transactional
    public Paiement payerCommande(Integer idCommande, Integer idCaisse, Paiement paiement, LocalDateTime dateMvtCaisse) {
        //* -- Get commande and proforma to check client/fournisseur
        Commande commande = commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));
        
        Proforma proforma = proformaRepository.findById(commande.getIdProforma())
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Validate montant against remaining to pay
        LocalDateTime checkDate = dateMvtCaisse != null ? dateMvtCaisse : LocalDateTime.now();
        BigDecimal reste = getMontantTotalPourUneCommande(idCommande, checkDate);
        if (paiement.getMontant() == null) {
            throw new IllegalArgumentException("Paiement montant is null");
        }
        if (paiement.getMontant().compareTo(reste) > 0) {
            throw new IllegalArgumentException("Montant du paiement dépasse le reste à payer: reste=" + reste);
        }

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

    public BigDecimal getMontantTotalPourUneCommande(Integer idCommande, LocalDateTime date) {
        // Sum total amount of the related proforma (prix * quantite)
        Commande commande = commandeRepository.findById(idCommande)
            .orElseThrow(() -> new IllegalArgumentException("Commande not found"));

        Integer idProforma = commande.getIdProforma();
        if (idProforma == null) {
            throw new IllegalArgumentException("Commande has no proforma linked");
        }

        BigDecimal totalProforma = proformaDetailRepository.sumTotalByProforma(idProforma);
        if (totalProforma == null) {
            totalProforma = BigDecimal.ZERO;
        }

        BigDecimal sommePaiements;
        if (date != null) {
            // Use date filter only if explicitly provided
            sommePaiements = paiementRepository.sumMontantByIdCommandeBeforeDate(idCommande, date);
        } else {
            // Default: sum all payments regardless of date
            sommePaiements = paiementRepository.sumMontantByIdCommande(idCommande);
        }
        if (sommePaiements == null) {
            sommePaiements = BigDecimal.ZERO;
        }

        BigDecimal reste = totalProforma.subtract(sommePaiements);
        return reste.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : reste;
    }

    public BigDecimal getTotalProforma(Integer idProforma) {
        if (idProforma == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = proformaDetailRepository.sumTotalByProforma(idProforma);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getSommePaiements(Integer idCommande) {
        BigDecimal sommePaiements = paiementRepository.sumMontantByIdCommande(idCommande);
        return sommePaiements != null ? sommePaiements : BigDecimal.ZERO;
    }
}