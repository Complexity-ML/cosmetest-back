package com.example.cosmetest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Web MVC minimale.
 * Les fichiers statiques sont servis automatiquement depuis /static.
 * Le routing SPA est géré par SpaController.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Configuration vide - Spring Boot gère tout automatiquement
    // Les fichiers statiques sont servis depuis src/main/resources/static/
    // Les controllers REST ont priorité sur les fichiers statiques
}
