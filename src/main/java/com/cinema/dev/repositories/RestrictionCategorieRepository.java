package com.cinema.dev.repositories;

import com.cinema.dev.models.RestrictionCategorie;
import com.cinema.dev.models.RestrictionCategorieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestrictionCategorieRepository extends JpaRepository<RestrictionCategorie, RestrictionCategorieId> {
    
    @Query("SELECT rc FROM RestrictionCategorie rc WHERE rc.id.idUtilisateur = :idUtilisateur")
    List<RestrictionCategorie> findByUtilisateur(@Param("idUtilisateur") Integer idUtilisateur);
    
    @Query("SELECT COUNT(rc) > 0 FROM RestrictionCategorie rc WHERE rc.id.idUtilisateur = :idUtilisateur")
    boolean hasRestrictions(@Param("idUtilisateur") Integer idUtilisateur);
    
    @Query("SELECT COUNT(rc) > 0 FROM RestrictionCategorie rc WHERE rc.id.idUtilisateur = :idUtilisateur AND rc.id.idCategorie = :idCategorie")
    boolean isAllowedForCategory(@Param("idUtilisateur") Integer idUtilisateur, @Param("idCategorie") Integer idCategorie);
    
    @Query("SELECT rc.id.idCategorie FROM RestrictionCategorie rc WHERE rc.id.idUtilisateur = :idUtilisateur")
    List<Integer> findAllowedCategoriesByUtilisateur(@Param("idUtilisateur") Integer idUtilisateur);
}
