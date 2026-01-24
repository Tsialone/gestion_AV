package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conf_va")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfVa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conf_va")
    private Integer idConfVa;

    @Column(name = "niveau_1", nullable = false)
    private Integer niveau1;

    @Column(name = "niveau_2", nullable = false)
    private Integer niveau2;

    @Column(name = "libelle", length = 50, nullable = false)
    private String libelle;
}
