package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventaire_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventaireDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventaire_detail")
    private Integer idInventaireDetail;

    @Column(name = "nombre", nullable = false)
    private Integer nombre;

    @Column(name = "id_depot", nullable = false)
    private Integer idDepot;

    @Column(name = "id_article", nullable = false)
    private Integer idArticle;

    @Column(name = "id_inventaire", nullable = false)
    private Integer idInventaire;
}
