package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.service.AuthService;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import com.example.cosmetest.domain.model.Identifiant;
import com.example.cosmetest.security.JwtTokenUtil;
import org.springframework.security.core.AuthenticationException;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final IdentifiantRepository identifiantRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());

    public AuthServiceImpl(IdentifiantRepository identifiantRepository,
            JwtTokenUtil jwtTokenUtil) {
        this.identifiantRepository = identifiantRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String authenticate(String login, String password) {
        try {
            Identifiant identifiant = identifiantRepository.findByIdentifiant(login)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            String springRole = mapRoleToSpringRole(identifiant.getRole());

            return jwtTokenUtil.generateToken(login, springRole);

        } catch (AuthenticationException e) {
            return null;
        }
    }

    private String mapRoleToSpringRole(String role) {
        if (role == null) {
            return "ROLE_GUEST";
        }

        // Gérer les rôles en chiffres (ancien format) pour rétrocompatibilité
        if (role.matches("\\d+")) {
            int roleId = Integer.parseInt(role);
            switch (roleId) {
                case 2:
                    return "ROLE_ADMIN";
                case 1:
                    return "ROLE_USER";
                default:
                    return "ROLE_GUEST";
            }
        }

        // Gérer les rôles en chaîne (nouveau format)
        switch (role.toUpperCase()) {
            case "ADMIN":
            case "MODERATEUR":
                return "ROLE_ADMIN";
            case "UTILISATEUR":
                return "ROLE_USER";
            default:
                return "ROLE_GUEST";
        }
    }

    @Override
    public boolean validateToken(String token) {
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        try {
            String username = jwtTokenUtil.extractUsername(token);

            if (username != null) {
                Identifiant identifiant = identifiantRepository.findByIdentifiant(username).orElse(null);
                return identifiant != null && !jwtTokenUtil.isTokenExpired(token);
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.extractUsername(token);
    }

    @Override
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }
}
