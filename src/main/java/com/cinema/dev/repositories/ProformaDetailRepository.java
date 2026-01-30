package com.cinema.dev.repositories;

import com.cinema.dev.models.ProformaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProformaDetailRepository extends JpaRepository<ProformaDetail, ProformaDetail.ProformaDetailId> {

	@Query("SELECT COALESCE(SUM(d.prix * d.quantite), 0) FROM ProformaDetail d WHERE d.id.idProforma = :idProforma")
	BigDecimal sumTotalByProforma(@Param("idProforma") Integer idProforma);

}