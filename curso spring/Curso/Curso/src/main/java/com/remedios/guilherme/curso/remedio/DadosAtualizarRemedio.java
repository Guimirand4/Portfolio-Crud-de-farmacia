package com.remedios.guilherme.curso.remedio;
import com.remedios.guilherme.curso.remedio.DadosAtualizarRemedio;


import jakarta.validation.constraints.NotNull;

public record DadosAtualizarRemedio(
		@NotNull
		Long id,
		String nome,
		Via via,
		Laboratorio laboratorio
		) {
	
}
