package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etat")
    private Integer idEtat;

    @Column(name = "libelle", length = 255)
    private String libelle;
}
