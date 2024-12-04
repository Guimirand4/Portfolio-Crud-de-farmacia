package com.remedios.guilherme.curso.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.remedios.guilherme.curso.remedio.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/remedios", produces = {"application/json"})
@Tag(name = "Remedios", description = "API para gerenciamento de remédios")
public class SwaggerController {

    private final RemedioRepository repository;

    public SwaggerController(RemedioRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Cadastra um novo remédio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Remédio cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoRemedio> cadastrar(@RequestBody @Valid DadosCadastroRemedio dados,
                                                              UriComponentsBuilder uriBuilder) {
        var remedio = new Remedio(dados);
        repository.save(remedio);
        var uri = uriBuilder.path("/remedios/{id}").buildAndExpand(remedio.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoRemedio(remedio));
    }

    @Operation(summary = "Lista todos os remédios ativos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<DadosListagemRemedio>> listar() {
        var lista = repository.findAllByAtivoTrue().stream().map(DadosListagemRemedio::new).toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Atualiza as informações de um remédio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Remédio atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Remédio não encontrado")
    })
    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoRemedio> atualizar(@RequestBody @Valid DadosAtualizarRemedio dados) {
        var remedio = repository.getReferenceById(dados.id());
        remedio.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoRemedio(remedio));
    }

    @Operation(summary = "Exclui permanentemente um remédio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Remédio excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Remédio não encontrado")
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Inativa um remédio sem excluí-lo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Remédio inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Remédio não encontrado")
    })
    @DeleteMapping("inativar/{id}")
    @Transactional
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        var remedio = repository.getReferenceById(id);
        remedio.inativar();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativa novamente um remédio inativo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Remédio ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Remédio não encontrado")
    })
    @PutMapping("ativar/{id}")
    @Transactional
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        var remedio = repository.getReferenceById(id);
        remedio.ativar();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Detalha informações de um remédio específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhamento retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Remédio não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoRemedio> detalhar(@PathVariable Long id) {
        var remedio = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoRemedio(remedio));
    }
}
