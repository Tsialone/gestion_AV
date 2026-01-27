package com.cinema.dev.models;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mvt_stock_lot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MvtStockLot {
    @EmbeddedId
    private MvtStockLotId idMvtStockLot;

    @Column(name = "qte")
    private Integer qte;

    @Embeddable
    @Data
    public static class MvtStockLotId implements Serializable {

        @Column(name = "id_mvt")
        private Integer idMvt;

        @Column(name = "id_lot")
        private Integer idLot;

    }
}
