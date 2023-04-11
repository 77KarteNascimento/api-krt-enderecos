package com.b4a.krt.end.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.b4a.krt.end.domain.exception.EntidadeNaoEcontradaException;
import com.b4a.krt.end.domain.model.Endereco;
import com.b4a.krt.end.domain.model.Usuario;
import com.b4a.krt.end.domain.repository.EnderecoRepository;
import com.b4a.krt.end.domain.repository.UsuarioRepository;
import com.b4a.krt.end.domain.service.EnderecoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	
	
	@GetMapping
	public List<Endereco> listar() {
        return enderecoRepository.findAll();
    }
	
	@GetMapping("{enderecoId}")
	public ResponseEntity<Endereco>  buscar(@PathVariable Long enderecoId) {
		return enderecoRepository.findById(enderecoId)
	 	.map(ResponseEntity::ok)
	 	.orElse(ResponseEntity.notFound().build());
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> adicionar(@Valid @RequestBody Endereco endereco) {
		try {
			endereco = enderecoService.salvar(endereco);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
			
		} catch (EntidadeNaoEcontradaException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
		
		
	}
	
	@PutMapping("/{enderecoId}")
	public ResponseEntity<Endereco> atualizar(@PathVariable Long enderecoId, @Valid @RequestBody Endereco endereco) {
		if (!enderecoRepository.existsById(enderecoId)) { 
			return ResponseEntity.notFound().build(); 
		}
		endereco.setId(enderecoId); 
		endereco = enderecoService.salvar(endereco); 
		
		return ResponseEntity.ok(endereco); 
	}
	
	@DeleteMapping("/{enderecoId}")
	public ResponseEntity<Void> remover(@PathVariable Long enderecoId) { 
		if (!enderecoRepository.existsById(enderecoId)) { 
		return ResponseEntity.notFound().build();
	}
		enderecoService.excluir(enderecoId); 
		
		return ResponseEntity.noContent().build(); 
		
	}

	@PostMapping("/usuarios/{usuarioId}/enderecos")
	public ResponseEntity<Endereco> adicionarEnderecoAoUsuario(@RequestBody Endereco novoEndereco,@PathVariable Long usuarioId) {
	    
	    Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
	    if (optionalUsuario.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    Usuario usuario = optionalUsuario.get();

	   
	    novoEndereco.setUsuario(usuario);
	    usuario.getEnderecos().add(novoEndereco);
	   
	    usuarioRepository.save(usuario);

	    return ResponseEntity.status(HttpStatus.CREATED)
	    		.body(novoEndereco);
	}
	
	
	@PatchMapping("/{enderecoId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long endedrecoId, @RequestBody Map<String, Object> campos) { 
		Endereco enderecoAtual = enderecoRepository.findById(endedrecoId)
				.orElse(null);
		
		
		if (enderecoAtual == null) {
			return ResponseEntity.notFound().build();			
		}
	
		merge(campos, enderecoAtual);
		
		return atualizar(endedrecoId, enderecoAtual);
	}

	private void merge(Map<String, Object> dadosOrigem, Endereco enderecoDestino) {
		ObjectMapper objectMapper = new ObjectMapper(); 
		Endereco enderecoOrigem = objectMapper.convertValue(dadosOrigem, Endereco.class);
		
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Endereco.class, nomePropriedade) ;
			
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, enderecoOrigem); 
			
			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			ReflectionUtils.setField(field, enderecoDestino, novoValor);
		});
	}
	


}
