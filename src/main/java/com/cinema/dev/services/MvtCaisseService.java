package com.cinema.dev.services;

import org.springframework.stereotype.Service;

import com.cinema.dev.models.MvtCaisse;
import com.cinema.dev.repositories.MvtCaisseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MvtCaisseService {
    private final MvtCaisseRepository mCaisseRepository;

    public Double getTotalCredit() {
        Double resp = 0.0;
        for (MvtCaisse mvtCaisse : mCaisseRepository.findAll()) {
            if (mvtCaisse.getCredit() == null) continue;
            resp +=   mvtCaisse.getCredit().doubleValue();

        }
        return resp;
    }

     public Double getTotalDebit() {
        Double resp = 0.0;
        for (MvtCaisse mvtCaisse : mCaisseRepository.findAll()) {
            if (mvtCaisse.getDebit() == null) continue;
            resp += mvtCaisse.getDebit().doubleValue();

        }
        return resp;
    }
}
