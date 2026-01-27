package com.cinema.dev.forms;

import java.time.LocalDate;
import java.util.HashMap;

import lombok.Data;

@Data
public class MvtStockForm {
    private LocalDate date;
    private boolean entrant;
    private String descriptionQualite;
    private String designation;
    private Integer idLivraison;
    private Integer idDepot;
    private Integer idAjustement;
    private HashMap<Integer , Integer> articleQte;
}
