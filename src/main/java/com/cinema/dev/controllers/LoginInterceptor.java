package com.cinema.dev.controllers;

import com.cinema.dev.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercepteur pour vérifier si l'utilisateur est connecté
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Autoriser les ressources statiques et la page de login
        if (uri.startsWith("/assets/") || 
            uri.startsWith("/login") || 
            uri.equals("/favicon.ico") ||
            uri.startsWith("/error")) {
            return true;
        }

        // Vérifier si l'utilisateur est connecté
        if (!sessionService.isLoggedIn(request.getSession())) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
