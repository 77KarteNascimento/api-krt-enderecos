package com.b4a.krt.end.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.b4a.krt.end.domain.exception.EntidadeNaoEcontradaException;
import com.b4a.krt.end.domain.exception.NegocioException;
import com.b4a.krt.end.domain.model.Endereco;
import com.b4a.krt.end.domain.model.Usuario;
import com.b4a.krt.end.domain.repository.EnderecoRepository;
import com.b4a.krt.end.domain.repository.UsuarioRepository;

@Service
public class EnderecoService {
	
	 @Autowired
	 private EnderecoRepository enderecoRepository;

	 @Autowired
	 private UsuarioRepository usuarioRepository;
	 
	 public Endereco salvar(Endereco endereco) {
			Long usuarioId = endereco.getUsuario().getId();
			
			Usuario usuario = usuarioRepository.findById(usuarioId)
					.orElseThrow(() -> new EntidadeNaoEcontradaException(
							String.format("Não existe cadastro de usuario com o código %d", usuarioId)));
			
			endereco.setUsuario(usuario);
			
			return enderecoRepository.save(endereco);
		}


	    public Endereco buscar(Long id) {
	        return enderecoRepository.findById(id)
	                .orElseThrow(() -> new NegocioException("Endereço não encontrado"));
	    }

	    public void excluir(Long id) {
	        enderecoRepository.deleteById(id);
	    }

}
