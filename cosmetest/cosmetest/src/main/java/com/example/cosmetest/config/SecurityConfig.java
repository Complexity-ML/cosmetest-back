package com.example.cosmetest.config;

import com.example.cosmetest.business.service.CustomUserDetailsService;
import com.example.cosmetest.security.JwtAuthenticationFilter;
import com.example.cosmetest.security.JwtAuthenticationEntryPoint;
import com.example.cosmetest.security.JwtCookieFilter;
import com.example.cosmetest.security.ApiAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtCookieFilter jwtCookieFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtCookieFilter jwtCookieFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtCookieFilter = jwtCookieFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authenticationProvider,
                                                   ApiAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
            .authenticationProvider(authenticationProvider)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configuration des autorisations
            .authorizeHttpRequests(auth -> auth
                // Authentification et documentation
                .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/logout").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/health").permitAll()
                // Fichiers statiques du frontend (SPA React/Vite)
                .requestMatchers("/", "/index.html", "/favicon.ico", "/vite.svg").permitAll()
                .requestMatchers("/assets/**").permitAll()
                // Routes SPA - permettre l'accès sans auth (le frontend gère sa propre auth)
                .requestMatchers("/login", "/login/**").permitAll()
                .requestMatchers("/dashboard", "/dashboard/**").permitAll()
                .requestMatchers("/etudes", "/etudes/**").permitAll()
                .requestMatchers("/volontaires", "/volontaires/**").permitAll()
                .requestMatchers("/planning", "/planning/**").permitAll()
                .requestMatchers("/admin", "/admin/**").permitAll()
                .requestMatchers("/settings", "/settings/**").permitAll()
                // Matrice minimale issue des règles déjà présentes dans les contrôleurs
                .requestMatchers(
                        "/api/audit/**",
                        "/api/connexions/**",
                        "/api/etude-volontaires/repair/**")
                .hasRole("ADMIN")
                // Toutes les autres requêtes API nécessitent une authentification
                .anyRequest().authenticated()
            )
            
            // IMPORTANT: Configuration du gestionnaire d'exceptions JWT
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            
            // CRITIQUE: Utiliser addFilterBefore (pas After) pour que JWT s'exécute en premier
            // 1) Nettoyage du cookie invalide/expiré au plus tôt
            .addFilterBefore(jwtCookieFilter, UsernamePasswordAuthenticationFilter.class)
            // 2) Authentification via JWT (headers/cookies)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("SecurityFilterChain configured with JWT authentication and exception handling");
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configuration CORS plus sécurisée mais flexible pour développement
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://192.168.*:*",
                "http://10.0.*:*",
                "http://intranet:*" // Intranet sur n'importe quel port
        ));
        
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type", 
                "X-Auth-Token",
                "Accept",
                "X-Requested-With",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder encoder = (DelegatingPasswordEncoder)
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // Les lignes historiques sans préfixe restent lisibles; les nouveaux
        // mots de passe sont encodés en BCrypt avec le préfixe {bcrypt}.
        encoder.setDefaultPasswordEncoderForMatches(new com.example.cosmetest.security.MD5PasswordEncoder());
        return encoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
                                                             PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsPasswordService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
