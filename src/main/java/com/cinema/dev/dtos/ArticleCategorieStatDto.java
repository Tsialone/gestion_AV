package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleCategorieStatDto {
    private String categorie;
    private Integer idCategorie;
    private Integer nbrArticlesVendus;

   
}
