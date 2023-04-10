package com.b4a.krt.end.domain.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b4a.krt.end.domain.exception.NegocioException;
import com.b4a.krt.end.domain.model.Endereco;
import com.b4a.krt.end.domain.model.Usuario;
import com.b4a.krt.end.domain.repository.EnderecoRepository;
import com.b4a.krt.end.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
    private EnderecoRepository enderecoRepository;
	
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
	
	 public void adicionarEndereco(Long idUsuario, Endereco endereco) {
	        Usuario usuario = usuarioRepository.findById(idUsuario)
	        		.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	        endereco = enderecoRepository.save(endereco);
	        usuario.getEnderecos().add(endereco);
	        usuarioRepository.save(usuario);
	    }
	
	@Transactional
	public void excluir(Long usuarioId) {
		usuarioRepository.deleteById(usuarioId);
		
	}
	
	
	
}
