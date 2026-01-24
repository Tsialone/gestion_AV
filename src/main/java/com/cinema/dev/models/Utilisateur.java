package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(name = "nom", length = 255)
    private String nom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Column(name = "id_depot")
    private Integer idDepot;

    @Column(name = "id_role", nullable = false)
    private Integer idRole;

    @Column(name = "id_dept", nullable = false)
    private Integer idDept;
}
