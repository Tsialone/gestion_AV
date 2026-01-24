package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "mvt_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MvtStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mvt")
    private Integer idMvt;

    @Column(name = "date_", nullable = false)
    private LocalDateTime date;

    @Column(name = "entrant", nullable = false)
    private Boolean entrant;

    @Column(name = "description_qualite", length = 200)
    private String descriptionQualite;

    @Column(name = "designation", length = 200)
    private String designation;

    @Column(name = "id_livraison", length = 50)
    private String idLivraison;

    @Column(name = "id_depot", nullable = false)
    private Integer idDepot;
}
