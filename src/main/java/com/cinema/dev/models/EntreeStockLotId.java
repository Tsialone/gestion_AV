package com.cinema.dev.models;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode // Important : JPA l'utilise pour comparer les entités
public class EntreeStockLotId implements Serializable {
    private Integer idMvt;
    private Integer idLot;
}