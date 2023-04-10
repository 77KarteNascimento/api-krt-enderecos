package com.b4a.krt.end.api.model.input;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClienteIdInput {
	
	@NotNull
	private Long id;
}
