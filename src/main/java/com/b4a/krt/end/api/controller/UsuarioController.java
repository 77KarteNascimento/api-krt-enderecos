package com.b4a.krt.end.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.b4a.krt.end.domain.model.Endereco;
import com.b4a.krt.end.domain.model.Usuario;
import com.b4a.krt.end.domain.repository.UsuarioRepository;
import com.b4a.krt.end.domain.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;	
	
	
	@GetMapping
	public List<Usuario> listar() {
		return usuarioRepository.findAll();
	}
	
	
	@GetMapping("{usuarioId}")
	public ResponseEntity<Usuario>  buscar(@PathVariable Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
	 	.map(ResponseEntity::ok)
	 	.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario adicionar(@Valid @RequestBody Usuario usuario) {
		return usuarioService.salvar(usuario);
		
	}
	
	@PutMapping("/{usuarioId}")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long usuarioId, @Valid @RequestBody Usuario usuario) {
		if (!usuarioRepository.existsById(usuarioId)) { 
			return ResponseEntity.notFound().build(); 
		}
		usuario.setId(usuarioId); 
		usuario = usuarioService.salvar(usuario); 
		
		return ResponseEntity.ok(usuario); 
	}
	
	@PostMapping("/{idUsuario}/enderecos")
    public void adicionarEndereco(@PathVariable Long idUsuario, @RequestBody Endereco endereco) {
        usuarioService.adicionarEndereco(idUsuario, endereco);
    }
	
	
	@DeleteMapping("/{usuarioId}")
	public ResponseEntity<Void> remover(@PathVariable Long usuarioId) { 
		if (!usuarioRepository.existsById(usuarioId)) { 
		return ResponseEntity.notFound().build();
	}
		usuarioService.excluir(usuarioId); 
		
		return ResponseEntity.noContent().build(); 
		
	}
	
	@PatchMapping("/{usuarioId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long usuarioId, @RequestBody Map<String, Object> campos) { //o Map represemta uma string como nome ou taxa frete e o Object qualquer Objeto
		Usuario usuarioAtual = usuarioRepository.findById(usuarioId)
				.orElse(null);
		
		
		if (usuarioAtual == null) {
			return ResponseEntity.notFound().build();			
		}
	
		merge(campos, usuarioAtual);
		
		return atualizar(usuarioId, usuarioAtual);
	}

	private void merge(Map<String, Object> dadosOrigem, Usuario usuarioDestino) {
		ObjectMapper objectMapper = new ObjectMapper(); 
		Usuario usuarioOrigem = objectMapper.convertValue(dadosOrigem, Usuario.class);
		
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Usuario.class, nomePropriedade) ;
			
			field.setAccessible(true);
			
			
			Object novoValor = ReflectionUtils.getField(field, usuarioOrigem); 
			
			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			ReflectionUtils.setField(field, usuarioDestino, novoValor);
		});
	}

}

