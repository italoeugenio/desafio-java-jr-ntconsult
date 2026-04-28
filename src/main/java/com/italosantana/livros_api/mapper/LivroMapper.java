package com.italosantana.livros_api.mapper;

import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.dtos.LivroResponseDTO;
import com.italosantana.livros_api.domain.entities.LivroModel;
import org.springframework.stereotype.Component;

@Component
public class LivroMapper {

    public LivroModel dtoRequestToModel(LivroRequestDTO dto){
        LivroModel model = new LivroModel();

        model.setTitulo(dto.titulo());
        model.setAutor(dto.autor());
        model.setAnoPublicacao(dto.anoPublicacao());

        return model;
    }

    public LivroResponseDTO modelToDtoResponse(LivroModel livroModel){
        return new LivroResponseDTO(
                livroModel.getId(),
                livroModel.getTitulo(),
                livroModel.getAutor(),
                livroModel.getAnoPublicacao()
        );
    }

}
