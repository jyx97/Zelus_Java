package br.com.fiap.Zelus.dto;

import br.com.fiap.Zelus.model.Abrigo.StatusAbrigo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AbrigoDTO (

    Long id,

    @NotBlank
    String nome,

    @Email
    @NotBlank
    String email,

    @NotBlank
    @Size(min = 10)
    String senha,

    @NotBlank
    @Size(min = 8, max = 9)
    String cep,

    StatusAbrigo status

) {}