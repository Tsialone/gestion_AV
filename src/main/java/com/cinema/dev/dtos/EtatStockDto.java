package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class EtatStockDto {

    private Long idDepot;
    private String depot;
    private Long idArticle;
    private String article;
    private Integer totalEntrees;
    private Integer totalSorties;
    private Integer stockADate;
}