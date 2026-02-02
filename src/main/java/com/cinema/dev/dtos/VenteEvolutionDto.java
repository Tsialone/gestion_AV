package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VenteEvolutionDto {
    private String date;
    private Integer nbrVente;
}
