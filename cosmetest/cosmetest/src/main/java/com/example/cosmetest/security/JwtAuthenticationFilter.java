package com.example.cosmetest.security;

import com.example.cosmetest.business.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Environnement: pour marquer le cookie Secure en prod
    private final boolean isProd = !"dev".equals(
            System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "dev"));

    // Seuil de rafraîchissement (si le token expire bientôt, on en émet un nouveau)
    private static final long REFRESH_THRESHOLD_SECONDS = 600; // 10 minutes

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            // Extraire le token depuis les headers Authorization (pas les cookies)
            String token = extractJwtFromRequest(request);
            
            logger.debug("Token extraite de la requête: {}", token != null ? "présent" : "absent");

            if (token != null) {
                try {
                    // Vérifier d'abord si le token est expiré
                    if (jwtTokenUtil.isTokenExpired(token)) {
                        logger.info("Token expiré détecté - renvoi 401");
                        sendUnauthorizedResponse(response, "Token expired");
                        return;
                    }

                    // Extraire le username
                    String username = jwtTokenUtil.extractUsername(token);
                    logger.debug("Username extrait du token: {}", username);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // Valider le token avec les détails utilisateur
                        if (jwtTokenUtil.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            logger.debug("Authentification réussie pour l'utilisateur: {}", username);

                            // Sliding refresh: si le token expire bientôt, émettre un nouveau token
                            long remainingSeconds = (jwtTokenUtil.extractExpiration(token).getTime() - System.currentTimeMillis()) / 1000;
                            if (remainingSeconds > 0 && remainingSeconds < REFRESH_THRESHOLD_SECONDS) {
                                try {
                                    String role = jwtTokenUtil.extractRole(token);
                                    String newToken = jwtTokenUtil.generateToken(username, role);

                                    long newTokenMaxAge = (jwtTokenUtil.extractExpiration(newToken).getTime() - System.currentTimeMillis()) / 1000;
                                    if (newTokenMaxAge < 0) newTokenMaxAge = 0;
                                    String cookie = "jwt=" + newToken
                                            + "; Path=/"
                                            + "; Max-Age=" + newTokenMaxAge
                                            + "; HttpOnly"
                                            + "; SameSite=Lax"
                                            + (isProd ? "; Secure" : "");
                                    response.addHeader("Set-Cookie", cookie);
                                    response.setHeader("Authorization", "Bearer " + newToken);
                                    logger.debug("Token rafraîchi pour l'utilisateur: {}", username);
                                } catch (Exception e) {
                                    logger.warn("Échec du rafraîchissement du token: {}", e.getMessage());
                                }
                            }
                        } else {
                            logger.warn("Token invalide pour l'utilisateur: {}", username);
                            sendUnauthorizedResponse(response, "Invalid token");
                            return;
                        }
                    }
                } catch (ExpiredJwtException e) {
                    logger.info("Token JWT expiré: {}", e.getMessage());
                    sendUnauthorizedResponse(response, "Token expired");
                    return;
                } catch (JwtException e) {
                    logger.warn("Token JWT invalide: {}", e.getMessage());
                    sendUnauthorizedResponse(response, "Invalid token format");
                    return;
                } catch (Exception e) {
                    logger.error("Erreur lors du traitement du token JWT: {}", e.getMessage());
                    sendUnauthorizedResponse(response, "Token processing error");
                    return;
                }
            }

        } catch (Exception e) {
            logger.error("Erreur inattendue dans JwtAuthenticationFilter: {}", e.getMessage());
            sendUnauthorizedResponse(response, "Authentication error");
            return;
        }

        // Continuer la chaîne de filtres
        chain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT depuis les headers Authorization OU les cookies
     * Priorité: Headers Authorization (mobile) puis cookies (web)
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // 1. Essayer d'abord les headers Authorization (React Native)
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Header Authorization: {}", bearerToken != null ? "présent" : "absent");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            logger.debug("Token trouvé dans les headers Authorization (mobile)");
            return bearerToken.substring(7); // Enlever "Bearer "
        }
        
        // 2. Si pas trouvé, essayer les cookies (React Web)
        String cookieToken = getJwtFromCookies(request);
        if (cookieToken != null) {
            logger.debug("Token trouvé dans les cookies (web)");
            return cookieToken;
        }
        
        logger.debug("Aucun token trouvé ni dans les headers ni dans les cookies");
        return null;
    }

    /**
     * Extrait le token JWT depuis les cookies (pour le frontend web)
     */
    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        
        for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Envoie une réponse 401 Unauthorized
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"error\":\"%s\",\"status\":401}", message));
    }
}
