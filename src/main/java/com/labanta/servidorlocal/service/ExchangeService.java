package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.ExchangeRateResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeService {

        private final RestTemplate restTemplate;

    public ExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Metodo para converter o preço
    public  Double converterPreco(Double precoEuros, String moedaDestino) {
        String url = "https://api.exchangerate-api.com/v4/latest/EUR";

        // Fazer a chamada HTTP (GET) à internet e guardar no nosso DTO
        ExchangeRateResponseDTO resposta = restTemplate.getForObject(url, ExchangeRateResponseDTO.class);

        // Ir ao mapa procurar a taxa da moeda pedida (ex: CVE)
        if (resposta != null && resposta.getRates().containsKey(moedaDestino)) {
            Double taxa = resposta.getRates().get(moedaDestino);
            return precoEuros * taxa; // Preço Original * Taxa de Câmbio
        }

        throw new RuntimeException("Moeda não suportada ou API indisponível.");
    }

}
