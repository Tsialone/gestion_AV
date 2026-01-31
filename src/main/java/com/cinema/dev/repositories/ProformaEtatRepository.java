package com.cinema.dev.repositories;

import com.cinema.dev.models.Proforma;
import com.cinema.dev.models.ProformaEtat;
import com.cinema.dev.models.ProformaEtat.ProformaEtatId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProformaEtatRepository extends JpaRepository<ProformaEtat, ProformaEtatId> {
    
    @Query("SELECT COUNT(pe) > 0 FROM ProformaEtat pe WHERE pe.id.idProforma = :idProforma AND pe.id.idEtat = :idEtat")
    boolean existsByIdProformaAndIdEtat(@Param("idProforma") Integer idProforma, @Param("idEtat") Integer idEtat);

    @Query("SELECT pe FROM ProformaEtat pe WHERE pe.id.idProforma = :idProforma")
    Proforma findByIdProforma(Integer idProforma);
}