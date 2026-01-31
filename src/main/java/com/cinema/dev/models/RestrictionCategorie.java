package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restriction_categorie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestrictionCategorie {
    @EmbeddedId
    private RestrictionCategorieId id;
    
    public RestrictionCategorie(Integer idCategorie, Integer idUtilisateur) {
        this.id = new RestrictionCategorieId(idCategorie, idUtilisateur);
    }
}
