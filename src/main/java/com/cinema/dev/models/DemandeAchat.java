package com.cinema.dev.models;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "demande_achat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeAchat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_da")
    private Integer idDa;

    @Column(name = "id_client")
    private Integer idClient;

        @Column(name = "date_demande")
    private LocalDate dateDemande;

}
