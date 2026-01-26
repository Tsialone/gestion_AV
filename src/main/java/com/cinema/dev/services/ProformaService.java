package com.cinema.dev.services;

import com.cinema.dev.models.*;
import com.cinema.dev.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class ProformaService {
    
    @Autowired
    private ProformaRepository proformaRepository;
    
    @Autowired
    private ProformaDetailRepository proformaDetailRepository;
    
    @Autowired
    private ProformaEtatRepository proformaEtatRepository;
    
    @Autowired
    private DemandeAchatRepository demandeAchatRepository;
    
    public List<Proforma> findAll() {
        return proformaRepository.findAll();
    }
    
    @Transactional
    public Proforma creerProforma(Integer idDemandeAchat, Integer idClient, Integer idFournisseur, 
                                  Proforma proforma, ProformaDetail[] proformaDetails, LocalDateTime dateCreation) {
        //? === RG: Validation ===
        //* -- idFournisseur null => idClient not null and vice versa
        if ((idFournisseur == null && idClient == null) || (idFournisseur != null && idClient != null)) {
            throw new IllegalArgumentException("Either idClient or idFournisseur must be provided, but not both");
        }
        
        //* -- If idClient in demandeAchat not null, idClient in proforma must be the same
        DemandeAchat da = demandeAchatRepository.findById(idDemandeAchat)
            .orElseThrow(() -> new IllegalArgumentException("DemandeAchat not found"));
        
        if (da.getIdClient() != null && !da.getIdClient().equals(idClient)) {
            throw new IllegalArgumentException("idClient in proforma must match idClient in demande_achat");
        }
        
        //* -- Set relationships
        proforma.setIdDa(idDemandeAchat);
        proforma.setIdClient(idClient);
        proforma.setIdFournisseur(idFournisseur);
        
        //* -- Insert proforma
        Proforma savedProforma = proformaRepository.save(proforma);
        
        //* -- Insert proforma_detail
        for (ProformaDetail detail : proformaDetails) {
            detail.getId().setIdProforma(savedProforma.getIdProforma());
            proformaDetailRepository.save(detail);
        }
        
        //* -- Insert proforma_etat (cree)
        ProformaEtat proformaEtat = new ProformaEtat();
        ProformaEtat.ProformaEtatId etatId = new ProformaEtat.ProformaEtatId();
        etatId.setIdProforma(savedProforma.getIdProforma());
        etatId.setIdEtat(1); // 1 is "cree" mayhaps
        proformaEtat.setId(etatId);
        proformaEtat.setDate(dateCreation != null ? dateCreation : LocalDateTime.now());
        proformaEtatRepository.save(proformaEtat);
        
        return savedProforma;
    }
    
    @Transactional
    public ProformaEtat validerProforma(Integer idProforma, LocalDateTime dateValidation) {
        //* -- Insert proforma_etat (valide)
        ProformaEtat proformaEtat = new ProformaEtat();
        ProformaEtat.ProformaEtatId etatId = new ProformaEtat.ProformaEtatId();
        etatId.setIdProforma(idProforma);
        etatId.setIdEtat(2); // 2 is "valide" me thinks
        proformaEtat.setId(etatId);
        proformaEtat.setDate(dateValidation != null ? dateValidation : LocalDateTime.now());
        
        return proformaEtatRepository.save(proformaEtat);
    }
}