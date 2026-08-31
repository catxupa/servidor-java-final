package com.labanta.servidorlocal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chaveSecreta;

    public JwtService(@Value("${jwt.secret}")String segredo){
        this.chaveSecreta= Keys.hmacShaKeyFor(segredo.getBytes());
    }

    public String gerarToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000 ))
                .signWith(chaveSecreta)
                .compact();
    }

    public String extrairUsername(String token) {
        return Jwts.parser()

                .verifyWith(chaveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }
}
