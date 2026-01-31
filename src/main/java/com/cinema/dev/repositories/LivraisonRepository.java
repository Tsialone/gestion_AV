package com.cinema.dev.repositories;

import com.cinema.dev.models.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Integer> {
}
