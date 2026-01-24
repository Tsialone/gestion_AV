package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "inventaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventaire")
    private Integer idInventaire;

    @Column(name = "date_inventaire", nullable = false)
    private LocalDate dateInventaire;
}
