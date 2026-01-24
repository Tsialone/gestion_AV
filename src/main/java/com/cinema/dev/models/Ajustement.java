package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ajustement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ajustement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ajustement")
    private Integer idAjustement;

    @Column(name = "date_ajurstement", nullable = false)
    private LocalDateTime dateAjustement;

    @Column(name = "id_mvt", nullable = false)
    private Integer idMvt;

    @Column(name = "id_inventaire", nullable = false)
    private Integer idInventaire;
}
