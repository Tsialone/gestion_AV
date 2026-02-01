package com.cinema.dev.repositories;

import com.cinema.dev.models.ConfVa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConfVaRepository extends JpaRepository<ConfVa, Integer> {
    
    /**
     * Find configuration by entity type (libelle)
     */
    Optional<ConfVa> findByLibelle(String libelle);
}
