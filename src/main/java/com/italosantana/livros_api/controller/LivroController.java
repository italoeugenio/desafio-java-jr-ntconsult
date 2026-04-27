package com.italosantana.livros_api.controller;

import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.dtos.LivroResponseDTO;
import com.italosantana.livros_api.service.LivroService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping("/")
    public ResponseEntity<LivroResponseDTO> salvarLivro(@Valid @RequestBody LivroRequestDTO data){
        log.info("Recebida requisição POST /salvando livro: {}", data.titulo());
        var response = livroService.salvarLivro(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
