package com.italosantana.livros_api.service;

import com.italosantana.livros_api.domain.dtos.LivroPaginacaoResponseDTO;
import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.dtos.LivroResponseDTO;
import com.italosantana.livros_api.domain.entities.LivroModel;
import com.italosantana.livros_api.exception.livro.LivroNaoEncontrado;
import com.italosantana.livros_api.exception.page.PaginaInvalidaException;
import com.italosantana.livros_api.mapper.LivroMapper;
import com.italosantana.livros_api.repository.LivroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Slf4j
public class LivroService {
    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;

    public LivroService(LivroRepository livroRepository, LivroMapper livroMapper) {
        this.livroRepository = livroRepository;
        this.livroMapper = livroMapper;
    }

    public LivroResponseDTO salvarLivro(LivroRequestDTO data) {
        log.info("Salvando novo livro com título: {}", data.titulo());
        LivroModel livroModel = livroMapper.dtoRequestToModel(data);
        livroRepository.save(livroModel);
        log.info("Livro salvo com sucesso. ID gerado: {}", livroModel.getId());
        return livroMapper.modelToDtoResponse(livroModel);
    }

    public LivroPaginacaoResponseDTO listarLivros(Integer numeroDaPagina) {
        log.info("Listagem de livros solicitada. Página recebida: {}", numeroDaPagina);
        if (numeroDaPagina <= 0) {
            log.warn("Número de página inválido recebido: {}", numeroDaPagina);
            throw new PaginaInvalidaException("O número da página não pode ser menor que 1");
        }

        Pageable pages = PageRequest.of(numeroDaPagina - 1, 60);
        Page<LivroResponseDTO> livros = livroRepository.findAll(pages)
                .map(livro -> this.livroMapper.modelToDtoResponse(livro));

        int totalDePaginas = livros.getTotalPages();
        return new LivroPaginacaoResponseDTO(livros.getContent(), totalDePaginas);
    }

    public LivroResponseDTO atualizarLivro(LivroRequestDTO data, Long id){
        LivroModel livro = livroRepository.findById(id).orElseThrow(() -> new LivroNaoEncontrado("Não encontramos nenhum livro com o id " + id));
        BeanUtils.copyProperties(data, livro);
        livro.setAtualizadoEm(LocalDateTime.now());
        livroRepository.save(livro);
        return this.livroMapper.modelToDtoResponse(livro);
    }

    public LivroResponseDTO encontrarPeloId(Long id){
        LivroModel livro = livroRepository.findById(id).orElseThrow(() -> new LivroNaoEncontrado("Não encontramos nenhum livro com o id " + id));
        return this.livroMapper.modelToDtoResponse(livro);
    }

    public void deletarLivroPeloId(Long id) {
        LivroModel livro = livroRepository.findById(id).orElseThrow(() -> new LivroNaoEncontrado("Não encontramos nenhum livro com o id " + id));
        livroRepository.delete(livro);
    }
}
