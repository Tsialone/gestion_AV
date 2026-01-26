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
        
        //* -- Insert proforma_etat (cree = 1)
        LocalDateTime date = dateCreation != null ? dateCreation : LocalDateTime.now();
        ProformaEtat proformaEtat = new ProformaEtat(savedProforma.getIdProforma(), 1, date);
        proformaEtatRepository.save(proformaEtat);
        
        return savedProforma;
    }
    
    @Transactional
    public ProformaEtat validerProforma(Integer idProforma, LocalDateTime dateValidation) {
        //* -- Check if proforma exists
        proformaRepository.findById(idProforma)
            .orElseThrow(() -> new IllegalArgumentException("Proforma not found"));
        
        //* -- Insert proforma_etat (valide = 2)
        LocalDateTime date = dateValidation != null ? dateValidation : LocalDateTime.now();
        ProformaEtat proformaEtat = new ProformaEtat(idProforma, 2, date);
        
        return proformaEtatRepository.save(proformaEtat);
    }
}