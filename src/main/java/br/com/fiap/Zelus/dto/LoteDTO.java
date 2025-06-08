package br.com.fiap.Zelus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoteDTO(
    Long id,

    @NotNull
    Double pesoI,

    @NotNull
    Double pesoFina,

    @NotNull
    Double temperatura,

    @NotBlank
    String estado,

    @NotNull
    Long abrigoId,

    AbrigoDTO abrigo
) {}