package com.cinema.dev.repositories;

import com.cinema.dev.dtos.LotCplDto;
import com.cinema.dev.models.LotCpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LotCplRepository extends JpaRepository<LotCpl, Long> {

    @Query("""
                SELECT new com.cinema.dev.dtos.LotCplDto(
                    v.idLot,
                    v.lot,
                    CAST(SUM(v.entree) AS integer),
                    CAST(SUM(v.sortie) AS integer)
                )
                FROM LotCpl v
                WHERE v.dateMouvement <= COALESCE(CAST(:dateMax AS timestamp), v.dateMouvement)
                GROUP BY v.idLot, v.lot
                ORDER BY v.lot
            """)
    List<LotCplDto> findLotCplFiltered(@Param("dateMax") LocalDateTime dateMax);

}
