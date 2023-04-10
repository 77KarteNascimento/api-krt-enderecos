package com.b4a.krt.end.api.model;

import java.util.List;

import com.b4a.krt.end.domain.model.Endereco;

import lombok.Data;

@Data
public class UsuarioModel {

	private Long id;
	private String nome;
	private String email;
	private List<Endereco> enderecos;
}
