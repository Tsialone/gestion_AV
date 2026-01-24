package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer idRole;

    @Column(name = "nom", length = 255)
    private String nom;

    @Column(name = "niveau", nullable = false)
    private Integer niveau;

    @Column(name = "seuil", nullable = false, precision = 15, scale = 2)
    private BigDecimal seuil;
}
