package com.cinema.dev.repositories;

import com.cinema.dev.models.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtatRepository extends JpaRepository<Etat, Integer> {
}