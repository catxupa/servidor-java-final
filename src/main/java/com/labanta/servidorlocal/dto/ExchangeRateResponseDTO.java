package com.labanta.servidorlocal.dto;

import java.util.Map;

public class ExchangeRateResponseDTO {

    private String base;
    private Map<String, Double> rates;

    public ExchangeRateResponseDTO(String base, Map<String, Double> rates) {
        this.base = base;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

}
