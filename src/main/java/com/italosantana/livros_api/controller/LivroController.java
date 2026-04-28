package com.italosantana.livros_api.controller;

import com.italosantana.livros_api.domain.dtos.LivroPaginacaoResponseDTO;
import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.dtos.LivroResponseDTO;
import com.italosantana.livros_api.service.LivroService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping("")
    public ResponseEntity<LivroResponseDTO> salvarLivro(@Valid @RequestBody LivroRequestDTO data){
        log.info("Recebida requisição POST /livros - livro: {}", data.titulo());
        var livro = livroService.salvarLivro(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(livro);
    }

    @GetMapping("")
    public ResponseEntity<LivroPaginacaoResponseDTO> listarTodos(
            @RequestParam(defaultValue = "1") Integer numeroDaPagina){
        log.info("Recebida requisição GET /livros - Página solicitada: {}", numeroDaPagina);
        return ResponseEntity.ok(livroService.listarLivros(numeroDaPagina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizarLivro(@Valid @RequestBody LivroRequestDTO data, @PathVariable("id") Long id){
        log.info("Recebida requisição PUT /livros/{} - Id solicitada: {}", id, id);
        var livroAtualizado = livroService.atualizarLivro(data, id);
        return ResponseEntity.status(HttpStatus.OK).body(livroAtualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> encontrarPeloId(@PathVariable("id") Long id){
        log.info("Recebida requisição GET /livros/{} - buscando livro por id", id);
        LivroResponseDTO livro = livroService.encontrarPeloId(id);
        return ResponseEntity.status(HttpStatus.OK).body(livro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivroPeloId(@PathVariable("id") Long id){
        log.info("Recebida requisição DELETE /livros/{} - Id solicitada: {}", id, id);
        livroService.deletarLivroPeloId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}