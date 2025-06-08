package br.com.fiap.Zelus.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote")
    private Long id;

    @Column(name = "peso_i", nullable = false)
    private Double pesoI;

    @Column(name = "peso_f")
    private Double pesoFina;

    @Column(nullable = false)
    private Double temperatura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLote estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_abrigo", nullable = false)
    private Abrigo abrigo;

    public enum EstadoLote {
    LACRADO,
    ABERTO
        }
}