package com.cinema.dev.repositories;

import com.cinema.dev.models.RestrictionFournisseur;
import com.cinema.dev.models.RestrictionFournisseurId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestrictionFournisseurRepository extends JpaRepository<RestrictionFournisseur, RestrictionFournisseurId> {
    
    @Query("SELECT rf FROM RestrictionFournisseur rf WHERE rf.id.idUtilisateur = :idUtilisateur")
    List<RestrictionFournisseur> findByUtilisateur(@Param("idUtilisateur") Integer idUtilisateur);
    
    @Query("SELECT COUNT(rf) > 0 FROM RestrictionFournisseur rf WHERE rf.id.idUtilisateur = :idUtilisateur")
    boolean hasRestrictions(@Param("idUtilisateur") Integer idUtilisateur);
    
    @Query("SELECT COUNT(rf) > 0 FROM RestrictionFournisseur rf WHERE rf.id.idUtilisateur = :idUtilisateur AND rf.id.idFournisseur = :idFournisseur")
    boolean isAllowedForFournisseur(@Param("idUtilisateur") Integer idUtilisateur, @Param("idFournisseur") Integer idFournisseur);
    
    @Query("SELECT rf.id.idFournisseur FROM RestrictionFournisseur rf WHERE rf.id.idUtilisateur = :idUtilisateur")
    List<Integer> findAllowedFournisseursByUtilisateur(@Param("idUtilisateur") Integer idUtilisateur);
}
