package com.labanta.servidorlocal.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ServicoNaoEncontradoException.class)
    public ResponseEntity <Map<String, String>>handlerServicoNaoEncontradoException(ServicoNaoEncontradoException  ex) {

        log.warn("tentativa de acesso a um recurso inexistente: {}", ex.getMessage());

        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", "recurso nao encontrado");
        resposta.put("detalhes",  ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(resposta);
    }

    // novo @ExceptionHandler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        log.warn("Desconto inválido recebido: {}", ex.getMessage());

        Map<String, String> erro = new HashMap<>();
        erro.put("erro", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }

    @ExceptionHandler(UtilizadorExistenteException.class)
    public ResponseEntity<Map<String, Object>> handleUtilizadorExistenteException(
            UtilizadorExistenteException ex) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("erro", "Bad Request");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}


