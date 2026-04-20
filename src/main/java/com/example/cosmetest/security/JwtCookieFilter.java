package com.example.cosmetest.security;

import com.example.cosmetest.business.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {
  private final AuthService authService;
  private final boolean isProd = !"dev".equals(
      System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "dev"));

  public JwtCookieFilter(AuthService authService) {
    this.authService = authService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
      throws ServletException, IOException {

    Cookie[] cookies = request.getCookies();
    String token = null;
    if (cookies != null) {
      for (Cookie c : cookies) {
        if ("jwt".equals(c.getName())) {
          token = c.getValue();
          break;
        }
      }
    }

    if (token != null) {
      try {
        // Valide/parse le token; si invalide, une exception sera levée par le service
        authService.getUsernameFromToken(token);
        // Ici, on ne set pas d'authentification: JwtAuthenticationFilter s'en charge.
      } catch (Exception ex) {
        // Token invalide/expiré -> supprimer le cookie
        String clear = "jwt=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax" + (isProd ? "; Secure" : "");
        response.addHeader("Set-Cookie", clear);
        // ne pas authentifier; les endpoints protégés renverront 401 via EntryPoint
      }
    }

    chain.doFilter(request, response);
  }
}
