package com.italosantana.livros_api.domain.entities;

import com.italosantana.livros_api.domain.dtos.LivroRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "livro")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Table(name = "TB_LIVROS")
public class LivroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "autor")
    private String autor;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    public void atualizar(LivroRequestDTO data) {
        boolean isAlterou = false;

        if (data.titulo() != null && !data.titulo().equals(this.titulo)) {
            this.titulo = data.titulo();
            isAlterou = true;
        }
        if (data.autor() != null && !data.autor().equals(this.autor)) {
            this.autor = data.autor();
            isAlterou = true;
        }
        if (data.anoPublicacao() != null && !data.anoPublicacao().equals(this.anoPublicacao)) {
            this.anoPublicacao = data.anoPublicacao();
            isAlterou = true;
        }

        if (isAlterou) {
            this.atualizadoEm = LocalDateTime.now();
        }
    }
}
