package com.cinema.dev.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cinema.dev.dtos.LotCplDto;
import com.cinema.dev.models.Depot;
import com.cinema.dev.repositories.DepotRepository;
import com.cinema.dev.services.DepotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/depot")
@RequiredArgsConstructor
public class DepotController {

    private final DepotService depotService;
    private final DepotRepository depotRepository;

    @GetMapping("/detail/{idDepot}")
    public String getDetail(
            @PathVariable Integer idDepot,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            Model model) {
        
        LocalDateTime filterDate = date != null ? date : LocalDateTime.now();
        String dateFormatted = filterDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        
        List<LotCplDto> lotsCpl = depotService.getLotsCplByIdDepotAndDateMax(idDepot, filterDate);
        
        Integer totalEntrees = lotsCpl.stream()
            .mapToInt(LotCplDto::getEntrees)
            .sum();
        
        Integer totalSorties = lotsCpl.stream()
            .mapToInt(LotCplDto::getSorties)
            .sum();
        
        Integer stockActuel = totalEntrees - totalSorties;

        Depot depot  = depotRepository.findById(idDepot).orElse(null);
        if (depot != null) {
            model.addAttribute("depotNom", depot.getNom());
        }
        
        model.addAttribute("lotsCpl", lotsCpl);
        model.addAttribute("idDepot", idDepot);
        model.addAttribute("totalEntrees", totalEntrees);
        model.addAttribute("totalSorties", totalSorties);
        model.addAttribute("stockActuel", stockActuel);
        model.addAttribute("date", dateFormatted);
        model.addAttribute("content", "pages/depot/depot-detail");
        return "admin-layout";
    }
}
