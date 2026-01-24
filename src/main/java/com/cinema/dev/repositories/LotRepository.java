package com.cinema.dev.repositories;

import com.cinema.dev.models.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Integer> {
    List<Lot> findByIdArticle(Integer idArticle);
}
