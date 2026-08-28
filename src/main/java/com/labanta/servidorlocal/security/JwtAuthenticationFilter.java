package com.labanta.servidorlocal.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Estrair o token (ignorar os premeiros 7 caracteres :"Bearer")
        String token = authHeader.substring(7);

        // Extrair o tokens vazios ou "undefined" (ex:frontend mal configurda)
        if (token.isEmpty() || token.equals("undefined")) {
           filterChain.doFilter(request, response);
            return;
        }
        try {
            //Extrair o username do token (isto tambem valida a assinatura e a expirracao)
            String username = jwtService.extrairUsername(token);


            // Se username e valido e ainda nao ha autenticacao no contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //Dizer ao spring que este utilizador esta autenticado

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            //token invalido ou expirado -nao autenticar, o Sring vai devolver 401
        }
        filterChain.doFilter(request, response);
    }
}