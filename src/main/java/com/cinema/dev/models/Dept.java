package com.cinema.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dept")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dept")
    private Integer idDept;

    @Column(name = "nom", length = 255)
    private String nom;
}
