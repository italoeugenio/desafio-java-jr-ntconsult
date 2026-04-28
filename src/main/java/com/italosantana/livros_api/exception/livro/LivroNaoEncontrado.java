package com.italosantana.livros_api.exception.livro;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LivroNaoEncontrado extends RuntimeException{
    public LivroNaoEncontrado(String mensagem){
        super(mensagem);
    }
}
