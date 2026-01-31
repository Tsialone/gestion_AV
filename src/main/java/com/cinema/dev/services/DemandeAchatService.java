package com.cinema.dev.services;

import com.cinema.dev.models.DemandeAchat;
import com.cinema.dev.models.DemandeAchatDetail;
import com.cinema.dev.repositories.DemandeAchatRepository;
import com.cinema.dev.repositories.DemandeAchatDetailRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemandeAchatService {
    
    @Autowired
    private DemandeAchatRepository demandeAchatRepository;
    
    @Autowired
    private DemandeAchatDetailRepository demandeAchatDetailRepository;
    
    public List<DemandeAchat> findAll() {
        return demandeAchatRepository.findAll();
    }
    
    public List<DemandeAchat> findWithFilters(Integer idClient, LocalDate startDate, LocalDate endDate) {
        return demandeAchatRepository.findWithFilters(idClient, startDate, endDate);
    }

    @Transactional
    public DemandeAchat effectuerDemandeAchat(Integer idClient, DemandeAchat demandeAchat, DemandeAchatDetail[] demandeAchatDetails) {
        //* -- Set idClient
        demandeAchat.setIdClient(idClient);
        
        //* -- Insert demande_achat
        DemandeAchat savedDa = demandeAchatRepository.save(demandeAchat);
        
        //* -- Insert demande_achat_detail
        for (DemandeAchatDetail detail : demandeAchatDetails) {
            detail.getId().setIdDa(savedDa.getIdDa());
            demandeAchatDetailRepository.save(detail);
        }
        
        return savedDa;
    }
}