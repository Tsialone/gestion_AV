package com.cinema.dev.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "mvt_caisse")
public class MvtCaisse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mvtc")
    private Integer idMvtc;

    @Column(name = "debit", precision = 15, scale = 2)
    private BigDecimal debit;

    @Column(name = "credit", precision = 15, scale = 2)
    private BigDecimal credit;

    @Column(name = "date_", nullable = false)
    private LocalDateTime date;

    @Column(name = "id_paiement", nullable = false)
    private Integer idPaiement;

    @Column(name = "id_caisse", nullable = false)
    private Integer idCaisse;

    public MvtCaisse() {
    }

    public MvtCaisse(BigDecimal debit, BigDecimal credit, LocalDateTime date, Integer idPaiement, Integer idCaisse) {
        this.debit = debit;
        this.credit = credit;
        this.date = date;
        this.idPaiement = idPaiement;
        this.idCaisse = idCaisse;
    }
 

    public Integer getIdMvtc() {
        return idMvtc;
    }

    public void setIdMvtc(Integer idMvtc) {
        this.idMvtc = idMvtc;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(Integer idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Integer getIdCaisse() {
        return idCaisse;
    }

    public void setIdCaisse(Integer idCaisse) {
        this.idCaisse = idCaisse;
    }
}
