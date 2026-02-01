package com.cinema.dev.repositories;

import com.cinema.dev.models.JournalMvtStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalMvtStockRepository extends JpaRepository<JournalMvtStock, Long> {

    @Query("""
        SELECT j
        FROM JournalMvtStock j
        WHERE j.dateMouvement <= :dateMax
        ORDER BY j.dateMouvement DESC, j.idMvt DESC
    """)
    List<JournalMvtStock> findJournalFiltered(@Param("dateMax") LocalDateTime dateMax);
}
