package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "v_sortie_stock_lot")
@IdClass(SortieStockLotId.class) // On lie la clé ici
@Getter
public class SortieStockLot {

    @Id
    @Column(name = "id_mvt")
    private Integer idMvt;

    @Id
    @Column(name = "id_lot")
    private Integer idLot;

    private Integer quantiteSortie;
    private String libelleLot;
    private Integer stockRestant_lot;
    private Integer quantiteInitialeLot;

    private Integer idArticle;
    private String libelleArticle;
    private Integer idCategorie;
    private String libelleCategorie;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;
    
    private String descriptionQualite;
    private String designation;
    private Integer idDepot;
    private String nomDepot;
}