package com.cinema.dev.services;

import com.cinema.dev.dtos.UtilisateurSessionDTO;
import com.cinema.dev.models.Dept;
import com.cinema.dev.models.Role;
import com.cinema.dev.models.Utilisateur;
import com.cinema.dev.repositories.DeptRepository;
import com.cinema.dev.repositories.RoleRepository;
import com.cinema.dev.repositories.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private static final String SESSION_USER_KEY = "currentUser";

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeptRepository deptRepository;

    /**
     * Connecte un utilisateur en le stockant dans la session
     */
    public UtilisateurSessionDTO login(HttpSession session, Integer idUtilisateur) {
        Utilisateur user = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + idUtilisateur));

        Role role = roleRepository.findById(user.getIdRole())
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé: " + user.getIdRole()));

        Dept dept = deptRepository.findById(user.getIdDept())
                .orElseThrow(() -> new RuntimeException("Département non trouvé: " + user.getIdDept()));

        UtilisateurSessionDTO sessionUser = new UtilisateurSessionDTO(
                user.getIdUtilisateur(),
                user.getNom(),
                role.getNom(),
                role.getNiveau(),
                dept.getNom(),
                dept.getIdDept(),
                role.getIdRole()
        );

        session.setAttribute(SESSION_USER_KEY, sessionUser);
        return sessionUser;
    }

    /**
     * Récupère l'utilisateur connecté depuis la session
     */
    public UtilisateurSessionDTO getCurrentUser(HttpSession session) {
        return (UtilisateurSessionDTO) session.getAttribute(SESSION_USER_KEY);
    }

    /**
     * Vérifie si un utilisateur est connecté
     */
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }

    /**
     * Déconnecte l'utilisateur (supprime de la session)
     */
    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        session.invalidate();
    }

    /**
     * Récupère l'ID de l'utilisateur connecté
     */
    public Integer getCurrentUserId(HttpSession session) {
        UtilisateurSessionDTO user = getCurrentUser(session);
        return user != null ? user.getIdUtilisateur() : null;
    }

    /**
     * Récupère tous les utilisateurs pour le sélecteur
     */
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
}
