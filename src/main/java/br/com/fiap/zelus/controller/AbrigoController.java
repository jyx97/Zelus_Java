package br.com.fiap.zelus.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.zelus.model.Abrigo;
import br.com.fiap.zelus.repository.AbrigoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/abrigo")
public class AbrigoController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AbrigoRepository repository;

    @GetMapping
    @Cacheable("abrigos")
    @Operation(tags = "Abrigo", summary = "Listar todos os abrigos")
    public List<Abrigo> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    @CacheEvict(value = "abrigos", allEntries = true)
    @Operation(tags = "Abrigo", summary = "Criar um novo abrigo",
        responses = {
            @ApiResponse(responseCode = "201", description = "Abrigo criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Abrigo já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Validação falhou")
        }
    )
    public ResponseEntity<Abrigo> criar(@RequestBody @Valid Abrigo abrigo) {
        if (abrigo.getId() != null && repository.existsById(abrigo.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Abrigo já cadastrado.");
        }
        var salvo = repository.save(abrigo);
        log.info("Abrigo criado: {}", salvo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    @Operation(tags = "Abrigo", summary = "Buscar abrigo por ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Abrigo encontrado"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado")
        }
    )
    public ResponseEntity<Abrigo> buscarPorId(@PathVariable Long id) {
        Abrigo abrigo = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado"));
        return ResponseEntity.ok(abrigo);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "abrigos", allEntries = true)
    @Operation(tags = "Abrigo", summary = "Atualizar abrigo por ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Abrigo atualizado"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Validação falhou")
        }
    )
    public ResponseEntity<Abrigo> atualizar(@PathVariable Long id, @RequestBody @Valid Abrigo atualizado) {
        var abrigo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado"));

        abrigo.setNome(atualizado.getNome());
        abrigo.setEmail(atualizado.getEmail());
        abrigo.setSenha(atualizado.getSenha());
        abrigo.setCep(atualizado.getCep());
        abrigo.setStatus(atualizado.getStatus());

        var salvo = repository.save(abrigo);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "abrigos", allEntries = true)
    @Operation(tags = "Abrigo", summary = "Remover abrigo por ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Abrigo removido"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado")
        }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        var abrigo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado"));

        repository.delete(abrigo);
        log.info("Abrigo deletado: {}", id);
    }
}
