package com.italosantana.livros_api.repository;

import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.entities.LivroModel;
import com.italosantana.livros_api.mapper.LivroMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class LivroRepositoryTest {

    @Autowired
    EntityManager entityManager;

    private LivroMapper livroMapper = new LivroMapper();

    @Test
    void findAll() {
    }

    private LivroModel criarLivro(LivroRequestDTO data){
        LivroModel newLivro = livroMapper.dtoRequestToModel(data);
        this.entityManager.persist(newLivro);
        return newLivro;
    }

}