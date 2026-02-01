package com.cinema.dev.services;

import com.cinema.dev.models.DemandeAchat;
import com.cinema.dev.models.DemandeAchatDetail;
import com.cinema.dev.repositories.DemandeAchatRepository;
import com.cinema.dev.repositories.DemandeAchatDetailRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemandeAchatService {
    
    @Autowired
    private DemandeAchatRepository demandeAchatRepository;
    
    @Autowired
    private DemandeAchatDetailRepository demandeAchatDetailRepository;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    public List<DemandeAchat> findAll() {
        return demandeAchatRepository.findAll();
    }
    
    public List<DemandeAchat> findWithFilters(Integer idClient, LocalDate startDate, LocalDate endDate) {
        return demandeAchatRepository.findWithFilters(idClient, startDate, endDate);
    }

    /**
     * Effectuer une demande d'achat
     * 
     * Authorization:
     * - Must be in 'Ventes' department
     * - Must have access to all articles (category restrictions)
     * 
     * @param idUtilisateur The user performing the action (required for authorization)
     * @param idClient Client ID (nullable for fournisseur demande)
     * @param demandeAchat The demande achat entity
     * @param demandeAchatDetails The details of the demande
     * @return The saved DemandeAchat
     */
    @Transactional
    public DemandeAchat effectuerDemandeAchat(Integer idUtilisateur, Integer idClient, DemandeAchat demandeAchat, DemandeAchatDetail[] demandeAchatDetails) {
        //* -- Authorization check
        List<Integer> articleIds = Arrays.stream(demandeAchatDetails)
            .map(d -> d.getId().getIdArticle())
            .collect(Collectors.toList());
        authorizationService.authorizeDemandeAchat(idUtilisateur, articleIds);
        
        //* -- Set idClient
        demandeAchat.setIdClient(idClient);
        
        //* -- Insert demande_achat
        DemandeAchat savedDa = demandeAchatRepository.save(demandeAchat);
        
        //* -- Insert demande_achat_detail
        for (DemandeAchatDetail detail : demandeAchatDetails) {
            detail.getId().setIdDa(savedDa.getIdDa());
            demandeAchatDetailRepository.save(detail);
        }
        
        //* -- Log to historique
        authorizationService.logAction(idUtilisateur, "demande_achat", "Création DA", savedDa.getIdDa());
        
        return savedDa;
    }
    
    /**
     * Legacy method without authorization (for backward compatibility)
     * @deprecated Use effectuerDemandeAchat(Integer idUtilisateur, ...) instead
     */
    @Deprecated
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