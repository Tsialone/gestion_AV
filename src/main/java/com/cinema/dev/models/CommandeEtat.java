package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "commande_etat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeEtat {
    @EmbeddedId
    private CommandeEtatId id;

    @Column(name = "date_", nullable = false)
    private LocalDateTime date;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommandeEtatId implements Serializable {
        @Column(name = "id_commande")
        private Integer idCommande;

        @Column(name = "id_etat")
        private Integer idEtat;
    }
}