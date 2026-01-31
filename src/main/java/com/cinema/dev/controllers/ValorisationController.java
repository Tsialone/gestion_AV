package com.cinema.dev.controllers;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cinema.dev.dtos.ValorisationStockRowDto;
import com.cinema.dev.services.AuthorizationService;
import com.cinema.dev.services.SessionService;
import com.cinema.dev.services.StockLotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/valorisations")
@RequiredArgsConstructor
public class ValorisationController {
    @Autowired
    StockLotService stockLotService;
    
    @Autowired
    AuthorizationService authorizationService;
    
    @Autowired
    SessionService sessionService;

    @GetMapping("cump") 
    public String cump(HttpSession session, Model model) throws Exception {
        // Check authorization - only Finance department can access valorisation
        Integer idUtilisateur = sessionService.getCurrentUserId(session);
        if (idUtilisateur != null) {
            authorizationService.authorizeValorisationStock(idUtilisateur);
        }
        
        List<ValorisationStockRowDto> result = this.stockLotService.getValorisationStockCUMP();
        for(int i = 0; i < result.size(); i++) {
            ValorisationStockRowDto row = result.get(i);
            System.out.println(row.toString() + "\n");
        }
        // model.addAttribute("articles", articleService.findAll());
       model.addAttribute("valeursStock", result); // On passe 'result', pas "result"
       model.addAttribute("valeurStock", result.getLast().getValeurStockLot()); // On passe 'result', pas "result"

       model.addAttribute("content", "pages/stock/valorisation-stock-cump");
        return "admin-layout";
    }

    @GetMapping("lifo") 
    public String fifo(HttpSession session, Model model) throws Exception {
        // Check authorization - only Finance department can access valorisation
        Integer idUtilisateur = sessionService.getCurrentUserId(session);
        if (idUtilisateur != null) {
            authorizationService.authorizeValorisationStock(idUtilisateur);
        }
        
        List<ValorisationStockRowDto> result = this.stockLotService.getValorisationStockLIFO();
        for(int i = 0; i < result.size(); i++) {
            ValorisationStockRowDto row = result.get(i);
            System.out.println(row.toString() + "\n");
        }
        // model.addAttribute("articles", articleService.findAll());
       model.addAttribute("valeursStock", result); // On passe 'result', pas "result"
       model.addAttribute("valeurStock", result.getLast().getValeurStockLot()); // On passe 'result', pas "result"

       model.addAttribute("content", "pages/stock/valorisation-stock-fifo");
        return "admin-layout";
    }

    @GetMapping("fifo") 
    public String lifo(HttpSession session, Model model) throws Exception {
        // Check authorization - only Finance department can access valorisation
        Integer idUtilisateur = sessionService.getCurrentUserId(session);
        if (idUtilisateur != null) {
            authorizationService.authorizeValorisationStock(idUtilisateur);
        }
        
        List<ValorisationStockRowDto> result = this.stockLotService.getValorisationStockFIFO();
        for(int i = 0; i < result.size(); i++) {
            ValorisationStockRowDto row = result.get(i);
            System.out.println(row.toString() + "\n");
        }
        // model.addAttribute("articles", articleService.findAll());
       model.addAttribute("valeursStock", result); // On passe 'result', pas "result"
       model.addAttribute("valeurStock", result.getLast().getValeurStockLot()); // On passe 'result', pas "result"

       model.addAttribute("content", "pages/stock/valorisation-stock-lifo");
        return "admin-layout";
    }
}
