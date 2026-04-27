package com.italosantana.livros_api.service;

import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.dtos.LivroResponseDTO;
import com.italosantana.livros_api.domain.entities.LivroModel;
import com.italosantana.livros_api.mapper.LivroMapper;
import com.italosantana.livros_api.repository.LivroRepository;
import org.springframework.stereotype.Service;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;

    public LivroService(LivroRepository livroRepository, LivroMapper livroMapper) {
        this.livroRepository = livroRepository;
        this.livroMapper = livroMapper;
    }


    public LivroResponseDTO salvarLivro(LivroRequestDTO data) {
        LivroModel livroModel = livroMapper.dtoRequestToModel(data);
        livroRepository.save(livroModel);
        return livroMapper.modelToDtoResponse(livroModel);
    }


}
