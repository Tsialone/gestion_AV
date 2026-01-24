package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "validation_ajustement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationAjustement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_va")
    private Integer idVa;

    @Column(name = "date_validation", nullable = false)
    private LocalDateTime dateValidation;

    @Column(name = "id_utilisateur", nullable = false)
    private Integer idUtilisateur;

    @Column(name = "id_ajustement", nullable = false)
    private Integer idAjustement;
}
