package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restriction_fournisseur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestrictionFournisseur {
    @EmbeddedId
    private RestrictionFournisseurId id;
    
    public RestrictionFournisseur(Integer idFournisseur, Integer idUtilisateur) {
        this.id = new RestrictionFournisseurId(idFournisseur, idUtilisateur);
    }
}
