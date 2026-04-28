package com.italosantana.livros_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.italosantana.livros_api.domain.dtos.LivroPaginacaoResponseDTO;
import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import com.italosantana.livros_api.domain.dtos.LivroResponseDTO;
import com.italosantana.livros_api.exception.livro.LivroNaoEncontrado;
import com.italosantana.livros_api.exception.page.PaginaInvalidaException;
import com.italosantana.livros_api.service.LivroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivroService livroService;

    @Test
    @DisplayName("Deve salvar um livro com sucesso e retornar 201")
    void salvarLivroCaso1() throws Exception {
        LivroRequestDTO request = new LivroRequestDTO("O Hobbit", "J.R.R. Tolkien", 1937);
        LivroResponseDTO response = new LivroResponseDTO(1L, "O Hobbit", "J.R.R. Tolkien", 1937);

        when(livroService.salvarLivro(any(LivroRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("O Hobbit"));
    }

    @Test
    @DisplayName("Retorna os livros e retonar 200")
    void listarTodosCaso1() throws Exception {
        List<LivroResponseDTO> listaDeLivros = List.of(
                new LivroResponseDTO(1L,"O Hobbit", "J.R.R. Tolkien", 1937),
                new LivroResponseDTO(2L,"O homem de Giz", "CJ Tudor", 2008)
        );

        LivroPaginacaoResponseDTO response = new LivroPaginacaoResponseDTO(listaDeLivros, 1);

        when(livroService.listarLivros(1)).thenReturn(response);

        mockMvc.perform(get("/livros")
                        .param("numeroDaPagina", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDePaginas").value(1))
                .andExpect(jsonPath("$.livros[0].titulo").value("O Hobbit"))
                .andExpect(jsonPath("$.livros[1].titulo").value("O homem de Giz"));

        verify(livroService, times(1)).listarLivros(1);
    }

    @Test
    @DisplayName("Retornar 400 ao passar número de página inválido")
    void listarTodosCaso2() throws Exception {
        when(livroService.listarLivros(-1)).thenThrow(new PaginaInvalidaException("O número da página não pode ser menor que 1"));

        mockMvc.perform(get("/livros")
                        .param("numeroDaPagina", "-1"))
                .andExpect(status().isBadRequest());

        verify(livroService, times(1)).listarLivros(-1);
    }

    @Test
    @DisplayName("Deve atualizar um livro com sucesso e retornar 200")
    void atualizarLivro() throws Exception {
        Long id = 1L;
        LivroRequestDTO request = new LivroRequestDTO("Novo Título", "J.R.R. Tolkien", 1937);
        LivroResponseDTO response = new LivroResponseDTO(1L, "Novo Título", "J.R.R. Tolkien", 1937);

//      when(livroService.atualizarLivro(any(LivroRequestDTO.class),id)).thenReturn(response); // quando usar o matcher em 1 parametro todos os outros tmb precisam ser
        when(livroService.atualizarLivro(any(LivroRequestDTO.class),eq(id))).thenReturn(response);

        mockMvc.perform(put("/livros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Novo Título"));

        verify(livroService, times(1)).atualizarLivro(any(LivroRequestDTO.class), eq(id));
    }

    @Test
    @DisplayName("Deve retonar um 404, pois tentou atualizar um livro com id que não está no sistema")
    void atualizarLivroCaso2() throws Exception {
        Long id = 427398L;
        LivroRequestDTO request = new LivroRequestDTO("Novo Título", "J.R.R. Tolkien", 1937);

        doThrow(new LivroNaoEncontrado("Não encontramos nenhum livro com o id " + id)).when(livroService).atualizarLivro(request, id);

        mockMvc.perform(put("/livros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).atualizarLivro(request, id);
    }

    @Test
    @DisplayName("Deve retornar o livro pelo ID e status 200")
    void encontrarPeloIdCaso1() throws Exception {
        Long id = 1L;
        LivroResponseDTO response = new LivroResponseDTO(1L, "O Hobbit", "J.R.R. Tolkien", 1937);

        when(livroService.encontrarPeloId(id)).thenReturn(response);

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("O Hobbit"));

        verify(livroService, times(1)).encontrarPeloId(id);
    }

    @Test
    @DisplayName("Livro não encontrado pelo ID, então deve retornar um 404")
    void encontrarPeloIdCaso2() throws Exception {
        Long id = 240L;

        doThrow(new LivroNaoEncontrado("Não encontramos nenhum livro com o id " + id)).when(livroService).encontrarPeloId(id);

        mockMvc.perform(get("/livros/{id}", id))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).encontrarPeloId(id);
    }

    @Test
    @DisplayName("Deve deletar um livro com sucesso e retornar 204")
    void deletarLivroPeloIdCaso1() throws Exception {
        Long id = 1L;
        doNothing().when(livroService).deletarLivroPeloId(id);

        mockMvc.perform(delete("/livros/{id}", id))
                .andExpect(status().isNoContent());

        verify(livroService, times(1)).deletarLivroPeloId(id);
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar deletar um livro inexistente")
    void deletarLivroPeloIdCaso2() throws Exception {
        Long id = 9943174981L;

        doThrow(new LivroNaoEncontrado("Não encontramos nenhum livro com o id " + id)).when(livroService).deletarLivroPeloId(id);

        mockMvc.perform(delete("/livros/{id}", id))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).deletarLivroPeloId(id);
    }

}