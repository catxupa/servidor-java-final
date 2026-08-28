package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.GeoLocationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoService {

    private final RestTemplate restTemplate;

    public GeoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoLocationResponseDTO localizarIp(String ip) {
        String url = "https://ipapi.co/" + ip + "/json/";

        GeoLocationResponseDTO resposta =
                restTemplate.getForObject(url, GeoLocationResponseDTO.class);

        return resposta;
    }

}
