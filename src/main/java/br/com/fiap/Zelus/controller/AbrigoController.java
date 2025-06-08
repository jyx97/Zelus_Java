package br.com.fiap.Zelus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.Zelus.model.Abrigo;
import br.com.fiap.Zelus.model.Abrigo.StatusAbrigo;
import br.com.fiap.Zelus.repository.AbrigoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/abrigo")
public class AbrigoController {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // DTO para retorno seguro (sem senha)
    public record AbrigoResponse(Long id, String nome, String email, String cep, StatusAbrigo status) {
        public static AbrigoResponse fromEntity(Abrigo abrigo) {
            return new AbrigoResponse(
                    abrigo.getId(),
                    abrigo.getNome(),
                    abrigo.getEmail(),
                    abrigo.getCep(),
                    abrigo.getStatus());
        }
    }

    @PostMapping
    @Operation(tags = "Abrigo", summary = "Criar novo abrigo", responses = {
            @ApiResponse(responseCode = "201", description = "Abrigo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    public ResponseEntity<AbrigoResponse> criar(@RequestBody @Valid Abrigo abrigo) {
        // Verifica se já existe abrigo com este email
        if (abrigoRepository.findByEmail(abrigo.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        // Criptografa a senha antes de salvar
        abrigo.setPassword(passwordEncoder.encode(abrigo.getPassword()));

        // Define o status como ATIVO por padrão, se não foi especificado
        if (abrigo.getStatus() == null) {
            abrigo.setStatus(StatusAbrigo.ATIVO);
        }

        Abrigo abrigoSalvo = abrigoRepository.save(abrigo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AbrigoResponse.fromEntity(abrigoSalvo));
    }

    @GetMapping
    @Operation(tags = "Abrigo", summary = "Listar abrigos com paginação", responses = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de abrigos retornada")
    })
    public ResponseEntity<Page<AbrigoResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Abrigo> abrigosPage = abrigoRepository.findAll(pageable);

        Page<AbrigoResponse> responsePage = abrigosPage.map(AbrigoResponse::fromEntity);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    @Operation(tags = "Abrigo", summary = "Buscar abrigo por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Abrigo encontrado"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado")
    })
    public ResponseEntity<AbrigoResponse> buscarPorId(@PathVariable Long id) {
        Abrigo abrigo = abrigoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado"));

        return ResponseEntity.ok(AbrigoResponse.fromEntity(abrigo));
    }

    @PutMapping("/{id}")
    @Operation(tags = "Abrigo", summary = "Atualizar abrigo", responses = {
            @ApiResponse(responseCode = "200", description = "Abrigo atualizado"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<AbrigoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid Abrigo abrigo) {
        Abrigo abrigoExistente = abrigoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado"));

        // Atualiza os campos
        abrigoExistente.setNome(abrigo.getNome());
        abrigoExistente.setCep(abrigo.getCep());
        abrigoExistente.setStatus(abrigo.getStatus());

        // Se a senha for enviada, atualiza e criptografa
        if (abrigo.getPassword() != null && !abrigo.getPassword().isEmpty()) {
            abrigoExistente.setPassword(passwordEncoder.encode(abrigo.getPassword()));
        }

        Abrigo abrigoSalvo = abrigoRepository.save(abrigoExistente);
        return ResponseEntity.ok(AbrigoResponse.fromEntity(abrigoSalvo));
    }

    @DeleteMapping("/{id}")
    @Operation(tags = "Abrigo", summary = "Remover abrigo", responses = {
            @ApiResponse(responseCode = "204", description = "Abrigo removido"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        Abrigo abrigo = abrigoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado"));

        abrigoRepository.delete(abrigo);
    }
}