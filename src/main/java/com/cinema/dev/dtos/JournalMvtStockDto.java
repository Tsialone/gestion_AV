package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalMvtStockDto {

    private Integer idMvt;
    private LocalDateTime dateMouvement;
    private Integer idDepot;
    private String depot;
    private Boolean entrant;
    private String typeMouvement;
    private String designation;
    private String descriptionQualite;
    private Integer idLivraison;
    private LocalDateTime dateLivraison;
    private Integer idArticle;
    private String article;
    private Integer idLot;
    private String lot;
    private Integer quantite;
}
