package com.italosantana.livros_api.handler;

import com.italosantana.livros_api.domain.dtos.ErroResponseDTO;
import com.italosantana.livros_api.exception.livro.LivroNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class LivroHandler {

    @ExceptionHandler(LivroNaoEncontrado.class)
    public ResponseEntity<ErroResponseDTO> handlerLivroNaoEncontrado(LivroNaoEncontrado exception) {
        ErroResponseDTO response = ErroResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .mensagem(exception.getMessage())
                .erros(List.of())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
