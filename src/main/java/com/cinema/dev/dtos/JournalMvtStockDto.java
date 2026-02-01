package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalMvtStockDto {

    private Long idMvt;
    private LocalDateTime dateMouvement;
    private Long idDepot;
    private String depot;
    private Boolean entrant;
    private String typeMouvement;
    private String designation;
    private String descriptionQualite;
    private Long idLivraison;
    private LocalDateTime dateLivraison;
    private Long idArticle;
    private String article;
    private Long idLot;
    private String lot;
    private Integer quantite;
}
