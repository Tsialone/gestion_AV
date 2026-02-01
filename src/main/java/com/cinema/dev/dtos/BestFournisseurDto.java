package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestFournisseurDto {
    private Integer id;
    private String nom;
    private Double montant;
}
