package com.italosantana.livros_api.domain.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotBlank(message = "Autor é obrigatório")
        String autor,

        @NotNull(message = "Ano é obrigatório")
        @Min(value = 1000, message = "O ano de publicação deve ser válido")
        @Max(value = 9999, message = "O ano de publicação deve ser válido")
        Integer anoPublicacao
) {
}
