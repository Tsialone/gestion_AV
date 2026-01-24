package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lot")
    private Integer idLot;

    @Column(name = "libelle", length = 255)
    private String libelle;

    @Column(name = "qte")
    private Integer qte;

    @Column(name = "id_article")
    private Integer idArticle;
}
