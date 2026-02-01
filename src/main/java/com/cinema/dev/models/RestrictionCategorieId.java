package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestrictionCategorieId implements Serializable {
    @Column(name = "id_categorie")
    private Integer idCategorie;
    
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;
}
