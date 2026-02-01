package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for displaying user in dropdowns with niveau info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDisplayDTO {
    private Integer idUtilisateur;
    private String nom;
    private Integer niveau;
    private String deptNom;
    private Integer idDept;
    
    /**
     * Returns display text: "nom (niveau X)"
     */
    public String getDisplayText() {
        return nom + " (niv." + niveau + ")";
    }
}
