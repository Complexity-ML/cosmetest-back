package com.example.cosmetest.presentation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class LegacyApiDeprecationFilter extends OncePerRequestFilter {

    private static final Map<String, String> LEGACY_TO_V1 = new LinkedHashMap<>();

    static {
        LEGACY_TO_V1.put("/api/etude-volontaires", "/api/v1/etude-volontaires");
        LEGACY_TO_V1.put("/api/volontaires-hc", "/api/v1/volontaires-hc");
        LEGACY_TO_V1.put("/api/volontaires", "/api/v1/volontaires");
        LEGACY_TO_V1.put("/api/etudes", "/api/v1/etudes");
        LEGACY_TO_V1.put("/api/rdvs", "/api/v1/rdvs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        LEGACY_TO_V1.entrySet().stream()
                .filter(entry -> uri.equals(entry.getKey()) || uri.startsWith(entry.getKey() + "/"))
                .findFirst()
                .ifPresent(entry -> {
                    response.setHeader("Deprecation", "true");
                    response.setHeader("Sunset", "Sat, 31 Jan 2027 23:59:59 GMT");
                    response.setHeader("Link", "<" + entry.getValue() + ">; rel=\"successor-version\"");
                });
        filterChain.doFilter(request, response);
    }
}
