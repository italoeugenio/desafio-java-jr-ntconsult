package com.italosantana.livros_api.handler;

import com.italosantana.livros_api.domain.dtos.ErroResponseDTO;
import com.italosantana.livros_api.exception.page.PaginaInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class PaginaInvalidaHandler {

    @ExceptionHandler(PaginaInvalidaException.class)
    public ResponseEntity<ErroResponseDTO> handlerPagina(PaginaInvalidaException exception){
        return new ResponseEntity<>(ErroResponseDTO
                .builder()
                .status(400)
                .mensagem(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .erros(List.of())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
