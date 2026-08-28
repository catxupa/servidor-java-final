package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.exception.UtilizadorExistenteException;
import com.labanta.servidorlocal.model.UtilizadorModel;
import com.labanta.servidorlocal.repository.UtilizadorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilizadorRepository utilizadorRepository;
    private final EmailService emailService;

    public AuthService(UtilizadorRepository utilizadorRepository, EmailService emailService) {
        this.utilizadorRepository = utilizadorRepository;
        this.emailService = emailService;
    }
         // registar utilizador
         public String registarUtilizador(
                 String username,
                 String password,
                 String email) {


             if (utilizadorRepository
                     .findByUsername(username)
                     .isPresent()) {

                 throw new UtilizadorExistenteException(
                         "Este username já está em uso, por favor escolha outro."
                 );
             }

             UtilizadorModel novoUtilizador = new UtilizadorModel(
                     username,
                     password,
                     email

             );

             //enviar email de boas vindas
             emailService.sendEmailBoasVindas(email, username);

             utilizadorRepository.save(novoUtilizador);

             return "Utilizador registado com sucesso!";
         }

}
