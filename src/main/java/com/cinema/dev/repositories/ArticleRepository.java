package com.cinema.dev.repositories;

import com.cinema.dev.dtos.ArticleCategorieStatDto;
import com.cinema.dev.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByLibelle(String libelle);

    @Query("SELECT new com.cinema.dev.dtos.ArticleCategorieStatDto(" +
            " c.libelle, cast(a.idCategorie as integer), cast(SUM(pd.quantite) as integer)) " +
            "FROM Proforma p " +
            "JOIN ProformaDetail pd ON p.idProforma = pd.id.idProforma " +
            "JOIN Article a ON pd.id.idArticle = a.idArticle " +
            "JOIN Categorie c ON a.idCategorie = c.idCategorie " +
            "WHERE p.idClient IS NOT NULL " +
            "GROUP BY a.idCategorie, c.libelle")
    List<ArticleCategorieStatDto> findNbrArticlesVendusByCategorie();

}
