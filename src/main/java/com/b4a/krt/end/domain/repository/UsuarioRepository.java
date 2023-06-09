package com.b4a.krt.end.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.b4a.krt.end.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	List<Usuario> findByNomeContaining(String nome);
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findById(Long Id);
}   

