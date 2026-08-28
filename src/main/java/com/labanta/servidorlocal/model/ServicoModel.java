package com.labanta.servidorlocal.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
public class ServicoModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        private long id;
        public String linkDonload;
        private String titulo;
        private String descricao;
        private Double preco;
        private Boolean estativo;
        private Double precoComDesconto;
        private String ImageK;


        public ServicoModel(){}

        public ServicoModel(String titulo, String descricao, Double preco, Boolean estativo, Double precoComDesconto){
            this.titulo = titulo;
            this.descricao = descricao;
            this.preco = preco;
            this.estativo = estativo;
            this.precoComDesconto = precoComDesconto;
            this.ImageK = ImageK;
        }

        public String getTitulo(){
            return this.titulo;
        }

        public void setTitulo(String titulo){
            this.titulo = titulo;
        }

        public String getDescricao(){
            return this.descricao;
        }

        public void setDescricao(String descricao){
            this.descricao = descricao;
        }

        public Double getPreco(){
            return this.preco;
        }

        public void setPreco(Double preco){
            this.preco = preco;
        }

        public Boolean getEstativo(){
            return this.estativo;
        }

        public void setEstativo(Boolean estativo){
            this.estativo = estativo;
        }

    public Double getPrecoComDesconto(){
        return this.precoComDesconto;
    }

    public void setPrecoComDesconto(Double precoComDesconto){
        this.precoComDesconto = precoComDesconto;
    }

    public String getImageK(){
        return this.ImageK;
    }

    public void setImageK(String ImageK){
        this.ImageK = ImageK;
    }


        public void aplicarDesconto(double percentagem ){
            double valorDesconto = (this.preco * percentagem) / 100;

            this.preco = this.preco - valorDesconto;

            System.out.println("Desconto aplicado com sucesso!");
            System.out.println("valor final:" + this.preco);
        }

        public void verificarDesponibilidade(){
            if (this.estativo){
                System.out.println("servico: " + this.estativo + " Servico esta ativo");

            } else {
                System.out.println("servico: " + this.estativo + " Servico nao esta ativo");
            }

        }
         
    
    }


