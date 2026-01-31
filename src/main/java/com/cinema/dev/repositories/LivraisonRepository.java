package com.cinema.dev.repositories;

import com.cinema.dev.models.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Integer> {
    boolean existsByIdCommande(Integer idCommande);
    Optional<Livraison> findByIdCommande(Integer idCommande);
}
