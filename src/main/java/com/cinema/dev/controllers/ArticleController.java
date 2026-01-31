package com.cinema.dev.controllers;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cinema.dev.utils.BreadcrumbItem;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ArticleController {

    // private final ArticleService articleService;
    @GetMapping
    public String getListe(Model model) {
        // model.addAttribute("articles", articleService.findAll());
        model.addAttribute("content", "pages/articles/article-liste");
        model.addAttribute("pageTitle", "Liste des Articles");
        model.addAttribute("breadcrumbs", Arrays.asList(
            new BreadcrumbItem("Articles", "/articles/liste")
        ));
        return "admin-layout";
    }
}
