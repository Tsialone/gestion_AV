package com.cinema.dev.repositories;

import com.cinema.dev.models.MvtCaisse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MvtCaisseRepository extends JpaRepository<MvtCaisse, Integer> {
}