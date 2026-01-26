package com.cinema.dev.repositories;

import com.cinema.dev.models.CommandeEtat;
import com.cinema.dev.models.CommandeEtat.CommandeEtatId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeEtatRepository extends JpaRepository<CommandeEtat, CommandeEtatId> {
    
    @Query("SELECT COUNT(ce) > 0 FROM CommandeEtat ce WHERE ce.id.idCommande = :idCommande AND ce.id.idEtat = :idEtat")
    boolean existsByIdCommandeAndIdEtat(@Param("idCommande") Integer idCommande, @Param("idEtat") Integer idEtat);
}
