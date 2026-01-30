package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caisse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caisse {
    @Id
    @Column(name = "id_caisse", length = 50)
    private Integer idCaisse;

    @Column(name = "lieu", length = 255)
    private String lieu;
}
