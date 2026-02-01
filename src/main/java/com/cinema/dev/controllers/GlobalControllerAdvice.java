package com.cinema.dev.controllers;

import com.cinema.dev.dtos.DeptUsersDTO;
import com.cinema.dev.dtos.UtilisateurSessionDTO;
import com.cinema.dev.models.Utilisateur;
import com.cinema.dev.services.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Injecte les données de session dans tous les modèles de vue
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private SessionService sessionService;

    /**
     * Ajoute l'utilisateur courant à tous les modèles
     */
    @ModelAttribute("currentUser")
    public UtilisateurSessionDTO currentUser(HttpSession session) {
        return sessionService.getCurrentUser(session);
    }

    /**
     * Ajoute la liste des utilisateurs pour le quick-switch
     */
    @ModelAttribute("allUtilisateurs")
    public List<Utilisateur> allUtilisateurs() {
        return sessionService.getAllUtilisateurs();
    }
    
    /**
     * Ajoute la liste des utilisateurs groupés par département
     */
    @ModelAttribute("usersGroupedByDept")
    public List<DeptUsersDTO> usersGroupedByDept() {
        return sessionService.getUtilisateursGroupedByDept();
    }

    /**
     * Indique si l'utilisateur est connecté
     */
    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(HttpSession session) {
        return sessionService.isLoggedIn(session);
    }
}
