package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour stocker les informations de l'utilisateur en session
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurSessionDTO {
    private Integer idUtilisateur;
    private String nom;
    private String roleNom;
    private Integer roleNiveau;
    private String deptNom;
    private Integer idDept;
    private Integer idRole;
    
    /**
     * Retourne un nom d'affichage formaté (ex: "Andry - Manager Ventes")
     */
    public String getDisplayName() {
        return nom;
    }
    
    /**
     * Retourne le rôle et département formatés
     */
    public String getRoleDisplay() {
        return roleNom + " - " + deptNom;
    }
}
