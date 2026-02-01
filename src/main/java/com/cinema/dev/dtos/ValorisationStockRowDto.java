package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValorisationStockRowDto {

    // --- Informations Temporelles et Références ---
    private LocalDateTime dateMouvement;
    private Integer idMvt;
    private String typeMouvement; // "ENTREE" ou "SORTIE"
    private String designation;   // Libellé du mouvement (ex: "Achat Fournisseur X")

    // --- Informations Article et Lot ---
    private Integer idArticle;
    private String libelleArticle;
    private String libelleCategorie;
    private Integer idLot;
    private String libelleLot;

    // --- Informations Quantitatives ---
    private Integer quantiteMouvement; // Quantité impactée par la ligne
    private Integer stockRestantLot;    // État du lot après ce mouvement
    private Integer quantiteInitialeLot;

    // --- Informations de Valorisation (Financier) ---
    private BigDecimal prixUnitaire;      // Prix d'achat unitaire (issu du proforma/achat)
    private BigDecimal valeurMouvement;   // quantiteMouvement * prixUnitaire
    private BigDecimal valeurStockLot;    // stockRestantLot * prixUnitaire (Valeur résiduelle du lot)
    
    // --- Informations Tiers (Optionnel) ---
    private String nomTier; // Nom du Fournisseur (si entrée) ou Client (si sortie)
    private String nomDepot;

    private Integer reste;

    private  Integer qttCumul;
    private  BigDecimal cump;

    
    
}