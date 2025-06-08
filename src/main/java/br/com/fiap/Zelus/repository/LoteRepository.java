package br.com.fiap.Zelus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.Zelus.model.Lote;

public interface LoteRepository extends JpaRepository<Lote, Long>, JpaSpecificationExecutor<Lote> {
    
    // Buscar todos os lotes de um abrigo específico
    List<Lote> findByAbrigoId(Long abrigoId);

    @Query("SELECT l FROM Lote l JOIN FETCH l.abrigo")
    List<Lote> findAllWithAbrigo();

    Optional<Lote> findTopByAbrigoIdAndIdLessThanOrderByIdDesc(Long abrigoId, Long id);
}