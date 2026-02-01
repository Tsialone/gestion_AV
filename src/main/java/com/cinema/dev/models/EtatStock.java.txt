package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "v_etat_stock") // nom exact de la vue
public class EtatStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_depot")
    private Long idDepot;

    @Column(name = "depot")
    private String depot;

    @Column(name = "id_article")
    private Long idArticle;

    @Column(name = "article")
    private String article;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "variation")
    private BigDecimal variation;

    @Column(name = "entree")
    private BigDecimal entree;

    @Column(name = "sortie")
    private BigDecimal sortie;

}
