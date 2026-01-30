package com.cinema.dev.repositories;

import com.cinema.dev.models.DemandeAchatDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface DemandeAchatDetailRepository extends JpaRepository<DemandeAchatDetail, DemandeAchatDetail.DemandeAchatDetailId> {
    
    @Query("SELECT d FROM DemandeAchatDetail d WHERE d.id.idDa = :idDa")
    List<DemandeAchatDetail> findByIdDa(@Param("idDa") Integer idDa);
    
    @Query("SELECT new map(a.libelle as libelle, d.quantite as quantite) " +
           "FROM DemandeAchatDetail d JOIN Article a ON d.id.idArticle = a.idArticle " +
           "WHERE d.id.idDa = :idDa")
    List<Map<String, Object>> findDetailsWithArticleNames(@Param("idDa") Integer idDa);
}