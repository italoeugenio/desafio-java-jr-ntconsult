package com.italosantana.livros_api.domain.dtos;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErroResponseDTO(
    int status,
    String mensagem,
    List<String> erros,
    LocalDateTime timestamp
) {

}
