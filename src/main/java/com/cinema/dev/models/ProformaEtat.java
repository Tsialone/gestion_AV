package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "proforma_etat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProformaEtat {
    @EmbeddedId
    private ProformaEtatId id;

    @Column(name = "date_", nullable = false)
    private LocalDateTime date;

    public ProformaEtat(Integer idProforma, Integer idEtat, LocalDateTime date) {
        this.id = new ProformaEtatId(idProforma, idEtat);
        this.date = date;
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProformaEtatId implements Serializable {
        @Column(name = "id_proforma")
        private Integer idProforma;

        @Column(name = "id_etat")
        private Integer idEtat;
    }
}