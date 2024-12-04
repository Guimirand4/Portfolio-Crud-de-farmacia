package com.remedios.guilherme.curso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remedios.guilherme.curso.infra.DadosTokenJWT;
import com.remedios.guilherme.curso.infra.TokenService;
import com.remedios.guilherme.curso.usuarios.DadosAutenticacao;
import com.remedios.guilherme.curso.usuarios.Usuario;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {
	
	@Autowired
    private final AuthenticationManager manager;
	
	@Autowired
	private TokenService tokenService;

    
    public AutenticacaoController(AuthenticationManager manager) {
        this.manager = manager;
    }

    @PostMapping
    public ResponseEntity<?> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var autenticacao = manager.authenticate(token);
        
        var tokenJWT = tokenService.gerarToken((Usuario) autenticacao.getPrincipal());
  
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

}
