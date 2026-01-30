package com.cinema.dev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class DashboardController {
    @GetMapping("/")
    public String goHome(Model model) {
        model.addAttribute("content", "pages/home");
        return "admin-layout";
    }
    
}