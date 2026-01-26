package com.cinema.dev.repositories;

import com.cinema.dev.models.Proforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProformaRepository extends JpaRepository<Proforma, Integer> {
}