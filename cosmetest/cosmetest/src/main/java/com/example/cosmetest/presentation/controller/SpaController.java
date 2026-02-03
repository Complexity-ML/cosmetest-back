package com.example.cosmetest.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller pour gérer le routing SPA (Single Page Application).
 * Redirige toutes les routes frontend vers index.html.
 * React Router gère ensuite le routing côté client.
 */
@Controller
public class SpaController {

    /**
     * Redirige les routes SPA vers index.html.
     * Exclut les routes /api/** qui sont gérées par les REST controllers.
     */
    @GetMapping({
        "/",
        "/login",
        "/login/**",
        "/dashboard",
        "/dashboard/**",
        "/etudes",
        "/etudes/**",
        "/volontaires",
        "/volontaires/**",
        "/planning",
        "/planning/**",
        "/admin",
        "/admin/**",
        "/settings",
        "/settings/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
