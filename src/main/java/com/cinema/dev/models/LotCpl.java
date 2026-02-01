package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "v_lot_cpl")
public class LotCpl {

    @Id
    @Column(name = "id_lot")
    private Long idLot;

    @Column(name = "lot")
    private String lot;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "entree")
    private Integer entree;

    @Column(name = "sortie")
    private Integer sortie;
}
