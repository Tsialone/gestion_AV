package com.cinema.dev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ArticleController {

    // private final ArticleService articleService;
    @GetMapping
    public String getListe(Model model) {
        // model.addAttribute("articles", articleService.findAll());
        model.addAttribute("content", "pages/home");
        return "admin-layout";
    }
}
