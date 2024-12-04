package com.remedios.guilherme.curso.infra;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication

@OpenAPIDefinition(info = @Info(title = "Remedios", version = "1", description = "API desenvolvida para testes do OpenApi"))
@EnableScheduling
public class SwaggerApplication {

	public static void main(String[] args) {
		
	}
}
