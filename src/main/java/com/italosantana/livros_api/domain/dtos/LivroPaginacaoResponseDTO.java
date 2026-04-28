package com.italosantana.livros_api.domain.dtos;

import java.util.List;

public record LivroPaginacaoResponseDTO(
        List<LivroResponseDTO> livros,
        int totalDePaginas
) {
}
