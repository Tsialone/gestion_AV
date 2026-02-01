package com.cinema.dev.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Embeddable
public class EtatStockId implements Serializable {

    @Column(name = "id_depot")
    private Integer idDepot;

    @Column(name = "id_article")
    private Integer idArticle;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;
}
