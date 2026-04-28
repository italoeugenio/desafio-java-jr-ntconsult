package com.italosantana.livros_api.config;

import com.italosantana.livros_api.domain.entities.LivroModel;
import com.italosantana.livros_api.repository.LivroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class SeedInicial {

    @Bean
    CommandLineRunner init(LivroRepository livroRepository) {
        return args -> {
            if (livroRepository.count() == 0) {
                livroRepository.saveAll(List.of(
                        new LivroModel(null, "O Hobbit", "J.R.R. Tolkien", 1937, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "A Sociedade do Anel", "J.R.R. Tolkien", 1954, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "Clean Code", "Robert C. Martin", 2008, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "O Guia do Mochileiro das Galáxias", "Douglas Adams", 1979, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "1984", "George Orwell", 1949, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "Dom Casmurro", "Machado de Assis", 1899, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "O Alquimista", "Paulo Coelho", 1988, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "Harry Potter e a Pedra Filosofal", "J.K. Rowling", 1997, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "O Senhor das Moscas", "William Golding", 1954, LocalDateTime.now(), LocalDateTime.now()),
                        new LivroModel(null, "A Metamorfose", "Franz Kafka", 1915, LocalDateTime.now(), LocalDateTime.now())
                ));
            }
        };
    }
}