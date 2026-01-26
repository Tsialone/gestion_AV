package com.cinema.dev.repositories;

import com.cinema.dev.models.CommandeEtat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeEtatRepository extends JpaRepository<CommandeEtat, CommandeEtat.CommandeEtatId> {
}