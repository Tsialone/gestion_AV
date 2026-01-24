package com.cinema.dev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    // private final ArticleService articleService;

    @GetMapping("/liste")
    public String getListe(Model model) {
        // model.addAttribute("articles", articleService.findAll());
        model.addAttribute("content", "pages/articles/article-liste");
        return "admin-layout";
    }
}
