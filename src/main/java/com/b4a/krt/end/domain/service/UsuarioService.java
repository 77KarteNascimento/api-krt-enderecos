package com.b4a.krt.end.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b4a.krt.end.api.domain.exception.NegocioException;
import com.b4a.krt.end.domain.model.Usuario;
import com.b4a.krt.end.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	public Usuario buscar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId) 
				.orElseThrow(() -> new NegocioException("Usuario não encontrado")); 
	}
	
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		boolean emailEmUso = usuarioRepository.findByEmail(usuario.getEmail()) 
			 .stream()       
			 .anyMatch(usuarioExistente -> !usuarioExistente.equals(usuario)); 
		
		if (emailEmUso) { 
			throw new NegocioException("Já existe um usuario cadastrado com este e-mail");
		}
		
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void excluir(Long usuarioId) {
		usuarioRepository.deleteById(usuarioId);
		
	}
	
	
	
}
