package com.cinema.dev.repositories;

import com.cinema.dev.models.ProformaEtat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProformaEtatRepository extends JpaRepository<ProformaEtat, ProformaEtat.ProformaEtatId> {
}