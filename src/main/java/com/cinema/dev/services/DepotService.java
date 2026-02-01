package com.cinema.dev.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinema.dev.dtos.LotCplDto;
import com.cinema.dev.repositories.DepotRepository;
import com.cinema.dev.repositories.LotCplRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepotService {
    private final DepotRepository depotRepository;
    private final LotCplRepository  lotCplRepository;

    public List<LotCplDto> getLotsCplByIdDepotAndDateMax(Integer idDepot, LocalDateTime dateMax) {
        List<LotCplDto> lotsCpl = lotCplRepository.findLotCplFiltered(dateMax);
        List<Integer> lotIds = depotRepository.findLotsIdsByIdDepot(idDepot);
        lotsCpl.removeIf(lotCpl -> !lotIds.contains(lotCpl.getIdLot().intValue()));   
        return lotsCpl;
    }

    public List<Integer> getLotsIdsByIdDepot(Integer idDepot) {
        List<Integer> lotIds = depotRepository.findLotsIdsByIdDepot(idDepot);
        return lotIds;
    }

}
