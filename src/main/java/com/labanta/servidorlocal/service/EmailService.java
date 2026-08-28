package com.labanta.servidorlocal.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailBoasVindas(String emailDestino, String nomeUtilizador) {
        
        //criar email simples
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestino);
        message.setSubject("Bem-vindo ao Marketplace");
        message.setText("Olá " + nomeUtilizador + "!\n\n" +
                "Sua conta foi criada com sucesso. Ja podes fazer login e explorar os nossos servicos ,\n\n" +
                "Com os melhores comprimentos,\n Equipa do Marketplace");

        // enviar
        mailSender.send(message);

    }
        public void enviarEmailOrcamento(String emailDestino, String nomeServico, Double precoConvertido, String moeda) {

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailDestino);
        mensagem.setSubject("O teu Orçamento do Marketplace");

        // Criar o texto do corpo do email
        String texto = String.format(
            "Olá!\n\nAqui tens o orçamento solicitado para o serviço:\n\n" +
            "Serviço: %s\n" +
            "Preço Final: %.2f %s\n\n" +
            "Este valor foi calculado com a taxa de câmbio em tempo real.\n" +
            "Obrigado por usares o nosso Marketplace!",
            nomeServico, precoConvertido, moeda
        );

        mensagem.setText(texto);
        mailSender.send(mensagem);
    }

    // enviar alerta de seguranca
    public void enviarEmailAlertaSeguranca(String emailDestino, String cidade, String pais) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Alerta de Segurança: Novo Login Detectado");
        mensagem.setText(
            "Olá!\n\n"
            + "Detectámos uma tentativa de login na tua conta a partir de um novo local.\n\n"
            + "Detalhes da Sessão:\n"
            + "- Cidade: " + cidade + "\n"
            + "- País: " + pais + "\n\n"
            + "Se não reconheceres esta atividade, por favor, altera a tua password imediatamente.\n\n"
            + "Com os melhores cumprimentos,\n"
            + "Equipa de Segurança do Marketplace"
        );
        mailSender.send(mensagem);
    }

}
