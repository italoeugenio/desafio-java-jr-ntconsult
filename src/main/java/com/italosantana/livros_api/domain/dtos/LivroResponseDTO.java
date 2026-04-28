package com.italosantana.livros_api.domain.dtos;

public record LivroResponseDTO(
        Long id,
        String titulo,
        String autor,
        Integer anoPublicacao
) {
}
