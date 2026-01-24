package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_general")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueGeneral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hg")
    private Integer idHg;

    @Column(name = "date_historique", nullable = false)
    private LocalDateTime dateHistorique;

    @Column(name = "nom_table", length = 50, nullable = false)
    private String nomTable;

    @Column(name = "desc_", length = 50, nullable = false)
    private String desc;

    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "id_utilisateur", nullable = false)
    private Integer idUtilisateur;
}
