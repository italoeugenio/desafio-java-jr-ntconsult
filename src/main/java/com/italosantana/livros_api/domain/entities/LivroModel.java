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

    public void atualizar(LivroRequestDTO data){
        this.titulo = data.titulo();
        this.autor = data.autor();
        this.anoPublicacao = data.anoPublicacao();
        this.atualizadoEm = LocalDateTime.now();
    }
}
