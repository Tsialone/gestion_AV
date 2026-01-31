package com.cinema.dev.repositories;

import com.cinema.dev.models.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {

	@Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.idCommande = :idCommande AND p.date <= :date")
	BigDecimal sumMontantByIdCommandeBeforeDate(@Param("idCommande") Integer idCommande, @Param("date") LocalDateTime date);


	@Query("SELECT p FROM Paiement p WHERE " +
		"CASE WHEN :idCommande IS NULL THEN true ELSE p.idCommande = :idCommande END " +
		"AND CASE WHEN CAST(:startDate AS timestamp) IS NULL THEN true ELSE p.date >= :startDate END " +
		"AND CASE WHEN CAST(:endDate AS timestamp) IS NULL THEN true ELSE p.date <= :endDate END " +
		"ORDER BY p.date DESC")
	List<Paiement> findWithFilters(
		@Param("idCommande") Integer idCommande,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}