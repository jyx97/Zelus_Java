package br.com.fiap.zelus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.zelus.model.Lote;

public interface LoteRepository extends JpaRepository<Lote, Long>, JpaSpecificationExecutor<Lote> {
    
    // Buscar todos os lotes de um abrigo específico
    List<Lote> findByAbrigoId(Long abrigoId);

    // Buscar todos os lotes já com os dados de abrigo carregados (evita lazy loading se for necessário)
    @Query("SELECT l FROM Lote l JOIN FETCH l.abrigo")
    List<Lote> findAllWithAbrigo();

    // Buscar o lote anterior ao atual pelo abrigo (o maior id menor que o id atual)
    Optional<Lote> findTopByAbrigoIdAndIdLessThanOrderByIdDesc(Long abrigoId, Long id);
}
