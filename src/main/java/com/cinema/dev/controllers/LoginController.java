package com.cinema.dev.controllers;

import com.cinema.dev.dtos.UtilisateurSessionDTO;
import com.cinema.dev.models.Utilisateur;
import com.cinema.dev.services.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private SessionService sessionService;

    /**
     * Page de login - sélection d'utilisateur
     */
    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        // Si déjà connecté, rediriger vers dashboard
        if (sessionService.isLoggedIn(session)) {
            return "redirect:/";
        }

        List<Utilisateur> utilisateurs = sessionService.getAllUtilisateurs();
        model.addAttribute("utilisateurs", utilisateurs);
        return "pages/auth/login";
    }

    /**
     * Action de login - sélection d'un utilisateur
     */
    @PostMapping("/login")
    public String doLogin(@RequestParam Integer idUtilisateur,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            UtilisateurSessionDTO user = sessionService.login(session, idUtilisateur);
            redirectAttributes.addFlashAttribute("toastMessage", "Bienvenue, " + user.getNom() + " !");
            redirectAttributes.addFlashAttribute("toastType", "success");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Erreur de connexion: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/login";
        }
    }

    /**
     * Quick switch - changer d'utilisateur rapidement depuis le header
     */
    @PostMapping("/switch-user")
    public String switchUser(@RequestParam Integer idUtilisateur,
                             @RequestHeader(value = "Referer", required = false) String referer,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            UtilisateurSessionDTO user = sessionService.login(session, idUtilisateur);
            redirectAttributes.addFlashAttribute("toastMessage", "Connecté en tant que " + user.getNom());
            redirectAttributes.addFlashAttribute("toastType", "info");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Erreur: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }

        // Retourner à la page précédente ou au dashboard
        if (referer != null && !referer.contains("/login")) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    /**
     * Logout - déconnexion
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        sessionService.logout(session);
        redirectAttributes.addFlashAttribute("toastMessage", "Déconnexion réussie");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/login";
    }
}
