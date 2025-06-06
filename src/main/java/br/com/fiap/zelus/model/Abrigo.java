package br.com.fiap.zelus.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "abrigo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Abrigo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abrigo")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 10) 
    @Column(nullable = false, length = 100)
    private String senha;

    @NotBlank
    @Size(min = 8, max = 9) // CEP com ou sem hífen
    @Column(nullable = false)
    private String cep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAbrigo status;


    public enum StatusAbrigo {
        ATIVO,
        INATIVO,
        BLOQUEADO
    }


}
