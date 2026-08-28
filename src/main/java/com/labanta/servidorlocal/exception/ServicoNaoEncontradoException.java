package com.labanta.servidorlocal.exception;



public class ServicoNaoEncontradoException extends RuntimeException{

    public ServicoNaoEncontradoException(String mensagem)
    {
        super (mensagem);
        
    }
}
