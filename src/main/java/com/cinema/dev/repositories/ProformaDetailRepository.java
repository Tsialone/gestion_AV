package com.cinema.dev.repositories;

import com.cinema.dev.dtos.BestClientDto;
import com.cinema.dev.dtos.BestFournisseurDto;
import com.cinema.dev.dtos.VenteEvolutionDto;
import com.cinema.dev.models.ProformaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface ProformaDetailRepository extends JpaRepository<ProformaDetail, ProformaDetail.ProformaDetailId> {

	@Query("SELECT new com.cinema.dev.dtos.VenteEvolutionDto(" +
			"CAST(p.dateDebut AS string), " +
			"CAST(SUM(pd.quantite) AS integer)) " +
			"FROM Proforma p " +
			"JOIN ProformaDetail pd ON p.idProforma = pd.id.idProforma " +
			"WHERE p.idClient IS NOT NULL " +
			"GROUP BY  p.dateDebut " +
			"ORDER BY CAST(p.dateDebut AS date)")
	List<VenteEvolutionDto> findVentesEvolution();

	@Query("SELECT COALESCE(SUM(d.quantite), 0) FROM ProformaDetail d " +
			"JOIN Proforma p ON d.id.idProforma = p.idProforma " +
			"WHERE p.idClient IS NOT NULL")
	Integer sumTotalArticlesVendus();

	@Query("SELECT COALESCE(SUM(d.prix * d.quantite), 0) FROM ProformaDetail d WHERE d.id.idProforma = :idProforma")
	BigDecimal sumTotalByProforma(@Param("idProforma") Integer idProforma);

	@Query("SELECT d FROM ProformaDetail d WHERE d.id.idProforma = :idProforma")
	List<ProformaDetail> findByIdProforma(@Param("idProforma") Integer idProforma);

	@Query("SELECT new map(a.libelle as libelle, d.quantite as quantite, d.prix as prix) " +
			"FROM ProformaDetail d JOIN Article a ON d.id.idArticle = a.idArticle " +
			"WHERE d.id.idProforma = :idProforma")
	List<Map<String, Object>> findDetailsWithArticleNames(@Param("idProforma") Integer idProforma);

	@Query("SELECT new com.cinema.dev.dtos.BestFournisseurDto(" +
			"f.idFournisseur, " +
			"f.nom, " +
			"CAST(SUM(pd.prix * pd.quantite) AS double)) " +
			"FROM Proforma p " +
			"JOIN ProformaDetail pd ON p.idProforma = pd.id.idProforma " +
			"JOIN Fournisseur f ON p.idFournisseur = f.idFournisseur " +
			"WHERE p.idFournisseur IS NOT NULL " +
			"GROUP BY f.idFournisseur, f.nom " +
			"ORDER BY SUM(pd.prix * pd.quantite) DESC")
	List<BestFournisseurDto> findBestFournisseurs(Pageable pageable);

	default List<BestFournisseurDto> findBestFournisseurs(Integer topN) {
		return findBestFournisseurs(PageRequest.of(0, topN));
	}

	@Query("SELECT new com.cinema.dev.dtos.BestClientDto(" +
			"c.idClient, " +
			"c.nom, " +
			"CAST(SUM(pd.prix * pd.quantite) AS double)) " +
			"FROM Proforma p " +
			"JOIN ProformaDetail pd ON p.idProforma = pd.id.idProforma " +
			"JOIN Client c ON p.idClient = c.idClient " +
			"WHERE p.idClient IS NOT NULL " +
			"GROUP BY c.idClient, c.nom " +
			"ORDER BY SUM(pd.prix * pd.quantite) DESC")
	List<BestClientDto> findBestClients(Pageable pageable);

	default List<BestClientDto> findBestClients(Integer topN) {
		return findBestClients(PageRequest.of(0, topN));
	}
}