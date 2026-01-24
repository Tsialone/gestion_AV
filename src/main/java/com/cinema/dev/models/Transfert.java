package com.cinema.dev.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfert")
public class Transfert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transfert")
    private Integer idTransfert;

    @Column(name = "date_transfert")
    private LocalDate dateTransfert;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(name = "mvt_cible", nullable = false)
    private Integer mvtCible;

    @Column(name = "mvt_origine", nullable = false)
    private Integer mvtOrigine;

    public Transfert() {
    }

    public Transfert(LocalDate dateTransfert, LocalDateTime dateValidation, Integer idUtilisateur, Integer mvtCible, Integer mvtOrigine) {
        this.dateTransfert = dateTransfert;
        this.dateValidation = dateValidation;
        this.idUtilisateur = idUtilisateur;
        this.mvtCible = mvtCible;
        this.mvtOrigine = mvtOrigine;
    }

    public Integer getIdTransfert() {
        return idTransfert;
    }

    public void setIdTransfert(Integer idTransfert) {
        this.idTransfert = idTransfert;
    }

    public LocalDate getDateTransfert() {
        return dateTransfert;
    }

    public void setDateTransfert(LocalDate dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getMvtCible() {
        return mvtCible;
    }

    public void setMvtCible(Integer mvtCible) {
        this.mvtCible = mvtCible;
    }

    public Integer getMvtOrigine() {
        return mvtOrigine;
    }

    public void setMvtOrigine(Integer mvtOrigine) {
        this.mvtOrigine = mvtOrigine;
    }
}
