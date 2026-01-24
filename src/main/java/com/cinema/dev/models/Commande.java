package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "commande")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_commande")
    private Integer idCommande;

    @Column(name = "date_", nullable = false)
    private LocalDateTime date;

    @Column(name = "remise", precision = 15, scale = 2)
    private BigDecimal remise;

    @Column(name = "id_proforma")
    private Integer idProforma;
}
