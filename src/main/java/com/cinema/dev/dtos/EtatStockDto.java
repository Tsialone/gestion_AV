package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class EtatStockDto {

    private Integer idDepot;
    private String depot;
    private Integer idArticle;
    private String article;
    private Integer totalEntrees;
    private Integer totalSorties;
    private Integer stockADate;
}