package com.example.algamoney.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
//	public Page<Categoria> findAll(Pageable pageable);
	
//	public Page<Categoria> findByCodigoGreaterThan(Pageable pageable, Long codigo);

}
