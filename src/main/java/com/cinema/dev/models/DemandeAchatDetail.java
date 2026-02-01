package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "demande_achat_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeAchatDetail {
    @EmbeddedId
    private DemandeAchatDetailId id;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DemandeAchatDetailId implements Serializable {
        @Column(name = "id_article")
        private Integer idArticle;

        @Column(name = "id_da")
        private Integer idDa;
    }
}