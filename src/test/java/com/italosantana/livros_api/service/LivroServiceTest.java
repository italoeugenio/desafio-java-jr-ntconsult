package com.italosantana.livros_api.service;

import com.italosantana.livros_api.domain.dtos.LivroPaginacaoResponseDTO;
import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.dtos.LivroResponseDTO;
import com.italosantana.livros_api.domain.entities.LivroModel;
import com.italosantana.livros_api.exception.livro.LivroNaoEncontrado;
import com.italosantana.livros_api.exception.page.PaginaInvalidaException;
import com.italosantana.livros_api.mapper.LivroMapper;
import com.italosantana.livros_api.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LivroMapper livroMapper;

    @InjectMocks
    private LivroService livroService;

    private LivroModel livroModel;
    private LivroRequestDTO livroRequestDTO;
    private LivroResponseDTO livroResponseDTO;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);

        livroModel = new LivroModel();
        livroModel.setId(1L);
        livroModel.setTitulo("O Hobbit");
        livroModel.setAutor("J.R.R. Tolkien");
        livroModel.setAnoPublicacao(1937);

        livroRequestDTO = new LivroRequestDTO("O Hobbit", "J.R.R. Tolkien", 1937);
        livroResponseDTO = new LivroResponseDTO(1L, "O Hobbit", "J.R.R. Tolkien", 1937);
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso quando tudo estiver ok")
    void salvarLivroCaso1() {
        when(livroMapper.dtoRequestToModel(livroRequestDTO)).thenReturn(livroModel);
        when(livroRepository.save(livroModel)).thenReturn(livroModel);
        when(livroMapper.modelToDtoResponse(livroModel)).thenReturn(livroResponseDTO);

        LivroResponseDTO resultado = livroService.salvarLivro(livroRequestDTO);

        assertEquals("O Hobbit", resultado.titulo());
        verify(livroRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de livros com sucesso")
    void listarLivrosCaso1() {
        Integer paginaValida = 1;
        List<LivroModel> listaDeLivro = List.of(livroModel);

        Pageable pageable = PageRequest.of(0, 60);
        Page<LivroModel> paginaMockada = new PageImpl<>(listaDeLivro, pageable, listaDeLivro.size());

        when(livroRepository.findAll(any(Pageable.class))).thenReturn(paginaMockada);
        when(livroMapper.modelToDtoResponse(livroModel)).thenReturn(livroResponseDTO);

        LivroPaginacaoResponseDTO resultado = livroService.listarLivros(paginaValida);

        assertEquals(1, resultado.totalDePaginas());
        assertEquals(1, resultado.livros().size());

        verify(livroRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve lançar uma PaginaInvalidaException quando a página for zero ou negativa")
    void listarLivrosCaso2() {
        Integer paginaInvalida = -1;

        Exception exception = assertThrows(
                PaginaInvalidaException.class, () -> livroService.listarLivros(paginaInvalida)
        );

        assertTrue(exception.getMessage().contains("O número da página não pode ser menor que 1"));
        verify(livroRepository, never()).findAll(any(Pageable.class));
    }


    @Test
    @DisplayName("Deve atualizar o livro de acordo com o id")
    void atualizarLivroCaso1() {
        LivroRequestDTO newLivro = new LivroRequestDTO("Senhor dos Anéis", "J.R.R. Tolkien", 1954);
        LivroResponseDTO responseAtualizado = new LivroResponseDTO(1L, "Senhor dos Anéis", "J.R.R. Tolkien", 1954);

        when(livroRepository.findById(livroModel.getId())).thenReturn(Optional.of(livroModel));
        when(livroRepository.save(livroModel)).thenReturn(livroModel);
        when(livroMapper.modelToDtoResponse(livroModel)).thenReturn(responseAtualizado);

        LivroResponseDTO resultado = livroService.atualizarLivro(newLivro, livroModel.getId());

        assertEquals("Senhor dos Anéis", resultado.titulo());
        verify(livroRepository, times(1)).save(any(LivroModel.class));
    }

    @Test
    @DisplayName("Deve lançar um exception de LivroNaoEncontrado")
    void atualizarLivroCaso2() {
        when(livroRepository.findById(livroModel.getId())).thenReturn(Optional.empty());

        LivroRequestDTO newLivro = new LivroRequestDTO("Senhor dos aneis", "J.R.R. Tolkien", 1430);

        Exception exception = assertThrows(
                LivroNaoEncontrado.class, () -> livroService.atualizarLivro(newLivro,livroModel.getId())
        );

        assertTrue(exception.getMessage().contains("Não encontramos nenhum livro com o id " + livroModel.getId()));
    }

    @Test
    @DisplayName("Deve retornar o livro de acordo com o id")
    void encontrarPeloIdCaso1() {
        when(livroRepository.findById(livroModel.getId())).thenReturn(Optional.of(livroModel));
        when(livroMapper.modelToDtoResponse(livroModel)).thenReturn(livroResponseDTO);

        LivroResponseDTO resultado = livroService.encontrarPeloId(livroModel.getId());

        assertEquals(1L, resultado.id());
        assertEquals("O Hobbit", resultado.titulo());
        verify(livroRepository, times(1)).findById(livroModel.getId());
    }

    @Test
    @DisplayName("Deve lançar um exception de LivroNaoEncontrado")
    void encontrarPeloIdCaso2() {
        when(livroRepository.findById(livroModel.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                LivroNaoEncontrado.class, () -> livroService.encontrarPeloId(livroModel.getId())
        );

        assertTrue(exception.getMessage().contains("Não encontramos nenhum livro com o id " + livroModel.getId()));
    }


    @Test
    @DisplayName("Deve deletar o livro de acordo com o id")
    void deletarLivroPeloIdCaso1() {
        when(livroRepository.findById(livroModel.getId())).thenReturn(Optional.of(livroModel));
        livroService.deletarLivroPeloId(livroModel.getId());
        verify(livroRepository, times(1)).delete(livroModel);
    }

    @Test
    @DisplayName("Deve lançar um exception de LivroNaoEncontrado")
    void deletarLivroPeloIdCaso2() {
        when(livroRepository.findById(livroModel.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                LivroNaoEncontrado.class, () -> livroService.deletarLivroPeloId(livroModel.getId())
        );

        assertTrue(exception.getMessage().contains("Não encontramos nenhum livro com o id " + livroModel.getId()));
    }
}