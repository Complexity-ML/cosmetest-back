package com.example.cosmetest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilitaire pour gérer les jetons JWT
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Extrait le nom d'utilisateur du jeton JWT
     *
     * @param token le jeton JWT
     * @return le nom d'utilisateur
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait la date d'expiration du jeton JWT
     *
     * @param token le jeton JWT
     * @return la date d'expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait une réclamation spécifique du jeton JWT
     *
     * @param token le jeton JWT
     * @param claimsResolver fonction pour extraire la réclamation
     * @return la réclamation
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait toutes les réclamations du jeton JWT
     *
     * @param token le jeton JWT
     * @return toutes les réclamations
     */
    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si le jeton JWT est expiré
     *
     * @param token le jeton JWT
     * @return true si le jeton est expiré, false sinon
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Génère un jeton JWT pour un utilisateur avec son rôle
     *
     * @param username le nom d'utilisateur
     * @param role le rôle de l'utilisateur
     * @return le jeton JWT
     */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    /**
     * Crée un jeton JWT
     *
     * @param claims les réclamations à inclure dans le jeton
     * @param subject le sujet du jeton (nom d'utilisateur)
     * @return le jeton JWT
     */
    private String createToken(Map<String, Object> claims, String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key)
                .compact();
    }

    /**
     * Valide un jeton JWT
     *
     * @param token le jeton JWT
     * @param userDetails les détails de l'utilisateur
     * @return true si le jeton est valide, false sinon
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrait le rôle du jeton JWT
     *
     * @param token le jeton JWT
     * @return le rôle de l'utilisateur
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
}