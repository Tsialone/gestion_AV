package com.cinema.dev.repositories;

import com.cinema.dev.models.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {

	@Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.idCommande = :idCommande AND p.date <= :date")
	BigDecimal sumMontantByIdCommandeBeforeDate(@Param("idCommande") Integer idCommande, @Param("date") LocalDateTime date);

}