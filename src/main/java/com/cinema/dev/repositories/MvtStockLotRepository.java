package com.cinema.dev.repositories;

import com.cinema.dev.models.MvtStockLot;
import com.cinema.dev.models.MvtStockLot.MvtStockLotId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MvtStockLotRepository extends JpaRepository<MvtStockLot, MvtStockLotId>{
    
}
