package com.cinema.dev.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cinema.dev.dtos.ValorisationStockRowDto;
import com.cinema.dev.services.StockLotService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/valorisations")
@RequiredArgsConstructor
public class ValorisationController {
    @Autowired
    StockLotService stockLotService;

    @GetMapping("cump") 
    public String cump(Model model) throws Exception {
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
    public String fifo(Model model) throws Exception {
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
    public String lifo(Model model) throws Exception {
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
