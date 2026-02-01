package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LotCplDto {

    private Long idLot;
    private String lot;
    private Integer entrees;
    private Integer sorties;
}
