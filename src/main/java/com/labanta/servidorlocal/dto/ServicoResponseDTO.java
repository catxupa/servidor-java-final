package com.labanta.servidorlocal.dto;


public class ServicoResponseDTO {

        private String titulo;
        private Double precoFinal;

        public ServicoResponseDTO(String titulo, Double precoFinal) {
            this.titulo = titulo;
            this.precoFinal = precoFinal;
        }

        public String getTitulo() {
            return titulo;
        }

        public Double getPrecoFinal() {
            return precoFinal;
        }
    }


