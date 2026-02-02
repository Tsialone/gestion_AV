package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

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
    
    // Restriction data
    private List<String> restrictedCategories = new ArrayList<>();  // Names of categories this user is restricted to
    private List<String> restrictedFournisseurs = new ArrayList<>();  // Names of fournisseurs this user is restricted to
    
    /**
     * Constructor without restrictions (for backward compatibility)
     */
    public UtilisateurSessionDTO(Integer idUtilisateur, String nom, String roleNom, 
            Integer roleNiveau, String deptNom, Integer idDept, Integer idRole) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.roleNom = roleNom;
        this.roleNiveau = roleNiveau;
        this.deptNom = deptNom;
        this.idDept = idDept;
        this.idRole = idRole;
        this.restrictedCategories = new ArrayList<>();
        this.restrictedFournisseurs = new ArrayList<>();
    }
    
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
    
    /**
     * Check if user has any restrictions
     */
    public boolean hasRestrictions() {
        return (restrictedCategories != null && !restrictedCategories.isEmpty()) 
            || (restrictedFournisseurs != null && !restrictedFournisseurs.isEmpty());
    }
}
