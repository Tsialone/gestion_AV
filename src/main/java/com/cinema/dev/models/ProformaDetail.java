package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "proforma_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProformaDetail {
    @EmbeddedId
    private ProformaDetailId id;

    @Column(name = "prix", nullable = false, precision = 15, scale = 2)
    private BigDecimal prix;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProformaDetailId implements Serializable {
        @Column(name = "id_article")
        private Integer idArticle;

        @Column(name = "id_proforma")
        private Integer idProforma;
    }

    public static Map<Integer, Integer> mapQuantiteParArticleStream(List<ProformaDetail> details) {
    return details.stream()
        .collect(Collectors.toMap(
            d -> d.getId().getIdArticle(), 
            ProformaDetail::getQuantite
        ));
}
}