package com.example.cosmetest.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserActionLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(UserActionLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String currentUser = "Anonyme";

        // Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            currentUser = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        logger.info("Utilisateur : {}, a accédé à : {}, méthode : {}", currentUser, path, method);

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Erreur lors du traitement de la requête pour l'utilisateur [{}] sur le chemin [{}]: {}",
                    currentUser, path, e.getMessage(), e);
            throw e; // Relancer l'exception pour permettre au reste de la stack de la gérer
        }
    }
}
