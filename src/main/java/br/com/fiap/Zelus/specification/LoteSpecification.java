package br.com.fiap.Zelus.specification;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.Zelus.dto.LoteDTO;
import br.com.fiap.Zelus.model.Lote;
import jakarta.persistence.criteria.Predicate;

public class LoteSpecification {

    public static Specification<Lote> comFiltros(LoteDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.abrigoId() != null) {
                predicates.add(cb.equal(root.get("abrigo").get("id"), filtro.abrigoId()));
            }

            if (filtro.estado() != null && !filtro.estado().isBlank()) {
                String estadoFiltro = filtro.estado().toLowerCase();

                // Se o filtro for exatamente "lacrado", faz busca exata
                if (estadoFiltro.equals("lacrado")) {
                    predicates.add(cb.equal(cb.lower(root.get("estado")), "lacrado"));
                } else {
                    // Senão, busca estados que contenham o texto informado (like)
                    predicates.add(cb.like(cb.lower(root.get("estado")), "%" + estadoFiltro + "%"));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}