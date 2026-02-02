package com.cinema.dev.controllers;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * Check if current user has access to this module (Finance or Direction only)
     */
    private String checkFinanceAccess(HttpSession session, RedirectAttributes redirectAttributes) {
        Integer userId = sessionService.getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        if (!authorizationService.isInFinanceOrDirection(userId)) {
            redirectAttributes.addFlashAttribute("toastMessage", 
                "Accès refusé. Seul le département Finance ou Direction peut accéder à la valorisation de stock.");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/";
        }
        return null; // Access granted
    }

    @GetMapping("cump") 
    public String cump(HttpSession session, RedirectAttributes redirectAttributes, Model model) throws Exception {
        String accessCheck = checkFinanceAccess(session, redirectAttributes);
        if (accessCheck != null) return accessCheck;
        
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
    public String fifo(HttpSession session, RedirectAttributes redirectAttributes, Model model) throws Exception {
        String accessCheck = checkFinanceAccess(session, redirectAttributes);
        if (accessCheck != null) return accessCheck;
        
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
    public String lifo(HttpSession session, RedirectAttributes redirectAttributes, Model model) throws Exception {
        String accessCheck = checkFinanceAccess(session, redirectAttributes);
        if (accessCheck != null) return accessCheck;
        
        List<ValorisationStockRowDto> result = this.stockLotService.getValorisationStockFIFO();
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
}
