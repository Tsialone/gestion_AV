package com.cinema.dev.repositories;

import com.cinema.dev.models.ProformaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface ProformaDetailRepository extends JpaRepository<ProformaDetail, ProformaDetail.ProformaDetailId> {

	@Query("SELECT COALESCE(SUM(d.prix * d.quantite), 0) FROM ProformaDetail d WHERE d.id.idProforma = :idProforma")
	BigDecimal sumTotalByProforma(@Param("idProforma") Integer idProforma);

	@Query("SELECT d FROM ProformaDetail d WHERE d.id.idProforma = :idProforma")
	List<ProformaDetail> findByIdProforma(@Param("idProforma") Integer idProforma);
	
	@Query("SELECT new map(a.libelle as libelle, d.quantite as quantite, d.prix as prix) " +
	       "FROM ProformaDetail d JOIN Article a ON d.id.idArticle = a.idArticle " +
	       "WHERE d.id.idProforma = :idProforma")
	List<Map<String, Object>> findDetailsWithArticleNames(@Param("idProforma") Integer idProforma);
}