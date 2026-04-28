package com.italosantana.livros_api.handler;

import com.italosantana.livros_api.domain.dtos.ErroResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;


@ControllerAdvice
@Slf4j
public class ValidationHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handlerValidation(MethodArgumentNotValidException exception){
        List<String> erros = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();


        ErroResponseDTO response = ErroResponseDTO.builder()
                .status(400)
                .mensagem("Erro de validação")
                .erros(erros)
                .timestamp(LocalDateTime.now())
                .build();
        log.warn("Erro na validacao da entrada: {}", erros);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
