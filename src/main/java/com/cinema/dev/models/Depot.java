package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "depot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Depot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depot")
    private Integer idDepot;

    @Column(name = "nom", length = 256)
    private String nom;
}
