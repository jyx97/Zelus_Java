package br.com.fiap.Zelus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.Zelus.dto.AbrigoDTO;
import br.com.fiap.Zelus.dto.LoteDTO;
import br.com.fiap.Zelus.exception.PesoInvalidoException;
import br.com.fiap.Zelus.model.Lote;
import br.com.fiap.Zelus.model.Lote.EstadoLote;
import br.com.fiap.Zelus.repository.LoteRepository;
import br.com.fiap.Zelus.specification.LoteSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoteService {

    @Autowired
    private LoteRepository repository;

    // Listar lotes filtrando e paginando
    public Page<LoteDTO> listarComFiltros(LoteDTO filtro, Pageable pageable) {
        var spec = LoteSpecification.comFiltros(filtro);
        return repository.findAll(spec, pageable).map(this::toDTO);
    }

    // Buscar por ID
    public Lote buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado com id: " + id));
    }

    // Salvar lote com regra para peso se estado for LACRADO
    public Lote salvar(Lote lote) {
        if (lote.getEstado() == EstadoLote.LACRADO) {
            if (lote.getPesoI() == null || lote.getPesoFina() == null || !lote.getPesoI().equals(lote.getPesoFina())) {
                throw new PesoInvalidoException("Peso inicial e peso final devem ser iguais para lotes lacrados.");
            }
        }
        return repository.save(lote);
    }

    // Conversão para DTO — torna público para uso no controller
    public LoteDTO toDTO(Lote lote) {
        var abrigo = lote.getAbrigo();

        return new LoteDTO(
            lote.getId(),
            lote.getPesoI(),
            lote.getPesoFina(),
            lote.getTemperatura(),
            lote.getEstado() != null ? lote.getEstado().name() : null,
            abrigo != null ? abrigo.getId() : null,
            abrigo != null ? new AbrigoDTO(
                abrigo.getId(),
                abrigo.getNome(),
                abrigo.getEmail(),
                abrigo.getCep(),
                abrigo.getStatus() != null ? abrigo.getStatus().name() : null,
                abrigo.getStatus()
            ) : null
        );
    }
}