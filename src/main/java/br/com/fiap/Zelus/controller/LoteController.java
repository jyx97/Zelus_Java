package br.com.fiap.Zelus.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import br.com.fiap.Zelus.dto.LoteDTO;
import br.com.fiap.Zelus.model.Abrigo;
import br.com.fiap.Zelus.model.Lote;
import br.com.fiap.Zelus.repository.AbrigoRepository;
import br.com.fiap.Zelus.repository.LoteRepository;
import br.com.fiap.Zelus.service.LoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lote")
@RequiredArgsConstructor
public class LoteController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private LoteService loteService;

    @GetMapping
    @Cacheable("lotesPaged")
    @Operation(tags = "Lote", summary = "Listar lotes com paginação e filtros", responses = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de lotes retornada")
    })
    public ResponseEntity<Page<LoteDTO>> listarPaginado(
            @RequestParam(required = false) Long abrigoId,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        LoteDTO filtro = new LoteDTO(null, null, null, null, estado, abrigoId, null);

        Page<LoteDTO> lotesPage = loteService.listarComFiltros(filtro, pageable);

        return ResponseEntity.ok(lotesPage);
    }

    @GetMapping("/abrigo/{abrigoId}")
    @Cacheable("lotesByAbrigo")
    @Operation(tags = "Lote", summary = "Listar lotes por abrigo", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de lotes retornada"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado")
    })
    public ResponseEntity<List<LoteDTO>> listarPorAbrigo(@PathVariable Long abrigoId) {
        if (!abrigoRepository.existsById(abrigoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado");
        }
        List<Lote> lotes = loteRepository.findByAbrigoId(abrigoId);
        List<LoteDTO> dtoList = lotes.stream().map(loteService::toDTO).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    @Operation(tags = "Lote", summary = "Buscar lote por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Lote encontrado"),
            @ApiResponse(responseCode = "404", description = "Lote não encontrado")
    })
    public ResponseEntity<LoteDTO> buscarPorId(@PathVariable Long id) {
        Lote lote = loteService.buscarPorId(id);
        LoteDTO dto = loteService.toDTO(lote);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = { "lotesByAbrigo", "lotes", "lotesPaged" }, allEntries = true)
    @Operation(tags = "Lote", summary = "Atualizar lote por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Lote atualizado"),
            @ApiResponse(responseCode = "404", description = "Lote não encontrado"),
            @ApiResponse(responseCode = "400", description = "Validação falhou")
    })
    public ResponseEntity<LoteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid Lote atualizado) {
        Lote lote = loteService.buscarPorId(id);

        lote.setPesoI(atualizado.getPesoI());
        lote.setPesoFina(atualizado.getPesoFina());
        lote.setTemperatura(atualizado.getTemperatura());
        lote.setEstado(atualizado.getEstado());

        Lote salvo = loteService.salvar(lote);
        LoteDTO dto = loteService.toDTO(salvo);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = { "lotesByAbrigo", "lotes", "lotesPaged" }, allEntries = true)
    @Operation(tags = "Lote", summary = "Remover lote por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Lote removido"),
            @ApiResponse(responseCode = "404", description = "Lote não encontrado")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));

        loteRepository.delete(lote);
        log.info("Lote deletado: {}", id);
    }

    @GetMapping("/com-abrigos")
    @Cacheable("lotes")
    @Operation(tags = "Lote", summary = "Listar lotes com dados do abrigo")
    public ResponseEntity<List<LoteDTO>> listarComAbrigos() {
        List<Lote> lotes = loteRepository.findAllWithAbrigo();
        List<LoteDTO> dtoList = lotes.stream().map(loteService::toDTO).toList();
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/abrigo/{abrigoId}")
    @CacheEvict(value = { "lotesByAbrigo", "lotes", "lotesPaged" }, allEntries = true)
    @Operation(tags = "Lote", summary = "Criar lote para um abrigo", responses = {
            @ApiResponse(responseCode = "201", description = "Lote criado"),
            @ApiResponse(responseCode = "404", description = "Abrigo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Validação falhou")
    })
    public ResponseEntity<LoteDTO> criarParaAbrigo(@PathVariable Long abrigoId, @RequestBody @Valid Lote novoLote) {
        Abrigo abrigo = abrigoRepository.findById(abrigoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abrigo não encontrado"));

        novoLote.setAbrigo(abrigo);
        Lote salvo = loteService.salvar(novoLote);
        log.info("Lote criado para o abrigo {}: lote {}", abrigoId, salvo.getId());

        LoteDTO dto = loteService.toDTO(salvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}