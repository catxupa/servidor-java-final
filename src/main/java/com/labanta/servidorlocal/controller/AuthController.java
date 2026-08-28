package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.GeoLocationResponseDTO;
import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.dto.RegistoRequestDTO;
import com.labanta.servidorlocal.model.UtilizadorModel;
import com.labanta.servidorlocal.repository.UtilizadorRepository;
import com.labanta.servidorlocal.security.JwtService;
import com.labanta.servidorlocal.service.AuthService;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.GeoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtilizadorRepository utilizadorRepository;
    private final JwtService jwtService;
    private final AuthService authService;
    private final GeoService geoService;
    private final EmailService emailService;

    public AuthController(
            UtilizadorRepository utilizadorRepository,
            JwtService jwtService,
            AuthService authService,
            GeoService geoService,
            EmailService emailService) {

        this.utilizadorRepository = utilizadorRepository;
        this.jwtService = jwtService;
        this.authService = authService;
        this.geoService = geoService;
        this.emailService = emailService;
    }

    @Operation(
        summary = "Registar um novo utilizador",
        description = "Regista um novo utilizador")

    @PostMapping("/registar")
    public ResponseEntity<Map<String, Object>> registar(
            @RequestBody RegistoRequestDTO dados) {

        String mensagem = authService.registarUtilizador(
                dados.getUsername(),
                dados.getPassword(),
                dados.getEmail()
        );

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.CREATED.value());
        resposta.put("mensagem", mensagem);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resposta);
    }

    @Operation(
        summary = "Login",
        description = "Login")

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginRequestDTO dados) {

        UtilizadorModel utilizador = utilizadorRepository
                .findByUsername(dados.getUsername())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Credenciais inválidas"
                        )
                );

        if (!utilizador.getPassword()
                .equals(dados.getPassword())) {

            throw new RuntimeException(
                    "Credenciais inválidas"
            );
        }

        String token = jwtService.gerarToken(
                utilizador.getUsername()
        );

        return ResponseEntity.ok(
                Map.of(
                        "mensagem",
                        "Login realizado com sucesso!",
                        "token",
                        token
                )
        );
    }

    // Alerta de login
    @Operation(
        summary = "Alerta de login",
        description = "Alerta de login")

    @PostMapping("/alerta-login")
    public ResponseEntity<String> alertaLogin(
        @RequestParam String email,
        @RequestParam String ip) {

    // 1. Passar o IP ao GeoService
    GeoLocationResponseDTO localizacao =
            geoService.localizarIp(ip);

    // 2. Passar cidade e país ao EmailService
    emailService.enviarEmailAlertaSeguranca(
            email,
            localizacao.getCity(),
            localizacao.getCountry_name()
    );

    // 3. Mensagem de sucesso
    return ResponseEntity.ok(
            "Alerta de segurança processado!"
    );
}

}
