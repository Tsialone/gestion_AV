package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EtatStockRecapDto {
        private String nom;
        private Double pourcentage;
}
