package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "proforma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proforma")
    private Integer idProforma;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(name = "id_da", nullable = false)
    private Integer idDa;

    @Column(name = "id_client")
    private Integer idClient;

    @Column(name = "id_fournisseur")
    private Integer idFournisseur;
}
