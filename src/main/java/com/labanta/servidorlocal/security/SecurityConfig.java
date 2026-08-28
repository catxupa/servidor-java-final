package com.labanta.servidorlocal.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
public class SecurityConfig {

    private final CustomsAuthenticationEntrypoint customsAuthenticationEntrypoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(CustomsAuthenticationEntrypoint customsAuthenticationEntrypoint, JwtAuthenticationFilter jwtAthenticationFilter){
        this.customsAuthenticationEntrypoint = customsAuthenticationEntrypoint;
        this.jwtAuthenticationFilter = jwtAthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(ex -> ex.authenticationEntryPoint(customsAuthenticationEntrypoint))

                .authorizeHttpRequests(auth -> auth
                        // Permite login e registo (cobre /api/auth/login, /api/auth/registar)
                        .requestMatchers("/api/auth/**").permitAll()
                        // GET de serviços e pesquisa livres
                        .requestMatchers(HttpMethod.GET, "/api/v1/servicos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/servicos/*/orcamento").authenticated()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        
                        .anyRequest().authenticated()

        )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://labanta.cv"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
