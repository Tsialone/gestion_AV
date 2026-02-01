package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "v_etat_stock") // nom exact de la vue
public class EtatStock {

    @EmbeddedId
    private EtatStockId id;

    @Column(name = "id_depot", insertable = false, updatable = false)
    private Integer idDepot;

    @Column(name = "depot")
    private String depot;

    @Column(name = "id_article", insertable = false, updatable = false)
    private Integer idArticle;

    @Column(name = "article")
    private String article;

    @Column(name = "date_mouvement", insertable = false, updatable = false)
    private LocalDateTime dateMouvement;

    @Column(name = "variation")
    private Integer variation;

    @Column(name = "entree")
    private Integer entree;

    @Column(name = "sortie")
    private Integer sortie;

}
