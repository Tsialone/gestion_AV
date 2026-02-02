package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Data
@Table(name = "v_journal_mvt_stock")
@IdClass(JournalMvtStockId.class) // On lie la clé ici
public class JournalMvtStock {

    @Id
    @Column(name = "id_mvt")
    private Integer idMvt;

    @Id
    @Column(name = "id_lot")
    private Integer idLot;
  

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "id_depot")
    private Integer idDepot;

    @Column(name = "depot")
    private String depot;

    @Column(name = "entrant")
    private Boolean entrant;

    @Column(name = "type_mouvement")
    private String typeMouvement;

    @Column(name = "designation")
    private String designation;

    @Column(name = "description_qualite")
    private String descriptionQualite;

    @Column(name = "id_livraison")
    private Integer idLivraison;

    @Column(name = "date_livraison")
    private LocalDateTime dateLivraison;

    @Column(name = "id_article")
    private Integer idArticle;

    @Column(name = "article")
    private String article;

    @Column(name = "lot")
    private String lot;
  

    @Column(name = "quantite")
    private Integer quantite;
}
