package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "livraison")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {
    @Id
    @Column(name = "id_livraison", length = 50)
    private String idLivraison;

    @Column(name = "date_", nullable = false)
    private LocalDateTime date;

    @Column(name = "id_commande", nullable = false)
    private Integer idCommande;
}
