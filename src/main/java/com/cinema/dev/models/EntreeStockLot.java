package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "v_entree_stock_lot")
@IdClass(EntreeStockLotId.class) // On lie la clé ici
@Getter
public class EntreeStockLot {

    @Id // JPA exige un ID. Si id_mvt n'est pas unique, il faudra une clé composée.
    @Column(name = "id_mvt")
    private Integer idMvt;

    @Id
    @Column(name = "id_lot")
    private Integer idLot;

    private Integer quantiteMouvement;
    private String libelleLot;
    private Integer stockActuelLot;
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

    private Integer idLivraison;
    private LocalDateTime dateLivraison;
    private Integer idCommande;
    private LocalDateTime dateCommande;
    private BigDecimal remise;

    private Integer idProforma;
    private LocalDateTime dateDebutProforma;
    private LocalDateTime dateFinProforma;
    private BigDecimal prixUnitaire;
    private Integer quantiteProforma;
    private BigDecimal prixTotalMouvement;

    private Integer idFournisseur;
    private String nomFournisseur;
    private Integer idClient;
    private String nomClient;

    
}