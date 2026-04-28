package com.italosantana.livros_api.repository;

import com.italosantana.livros_api.domain.entities.LivroModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Long> {
    Page<LivroModel> findAll(Pageable pageable);
}
