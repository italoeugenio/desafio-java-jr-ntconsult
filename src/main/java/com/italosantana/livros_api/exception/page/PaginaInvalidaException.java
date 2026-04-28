package com.italosantana.livros_api.exception.page;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaginaInvalidaException extends RuntimeException{
    public PaginaInvalidaException(String mensagem){
        super(mensagem);
    }
}
