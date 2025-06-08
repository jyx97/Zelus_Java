package br.com.fiap.Zelus.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fiap.Zelus.model.Abrigo;
import br.com.fiap.Zelus.model.Abrigo.StatusAbrigo;
import br.com.fiap.Zelus.model.Lote;
import br.com.fiap.Zelus.model.Lote.EstadoLote;
import br.com.fiap.Zelus.repository.AbrigoRepository;
import br.com.fiap.Zelus.repository.LoteRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataSeeder {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (abrigoRepository.count() > 0 || loteRepository.count() > 0) {
            System.out.println("Seed ignorado: dados já existem.");
            return;
        }

        // Criando abrigos com senha criptografada
        var abrigo1 = Abrigo.builder()
            .nome("Banco de Alimentos SP")
            .email("sp@abrigo.org")
            .senha(passwordEncoder.encode("123456"))
            .cep("01001-000")
            .status(StatusAbrigo.ATIVO)
            .build();

        var abrigo2 = Abrigo.builder()
            .nome("Centro Solidário RJ")
            .email("rj@abrigo.org")
            .senha(passwordEncoder.encode("123456"))
            .cep("20040-020")
            .status(StatusAbrigo.ATIVO)
            .build();

        var abrigo3 = Abrigo.builder()
            .nome("Doação Sul")
            .email("sul@abrigo.org")
            .senha(passwordEncoder.encode("123456"))
            .cep("30110-010")
            .status(StatusAbrigo.INATIVO)
            .build();

        abrigoRepository.saveAll(List.of(abrigo1, abrigo2, abrigo3));

        // Criando lotes
        var lote1 = Lote.builder()
            .pesoI(25.0)
            .pesoFina(15.0)
            .temperatura(5.0)
            .estado(EstadoLote.ABERTO)
            .abrigo(abrigo1)
            .build();

        var lote2 = Lote.builder()
            .pesoI(30.0)
            .pesoFina(30.0)
            .temperatura(7.0)
            .estado(EstadoLote.LACRADO)
            .abrigo(abrigo2)
            .build();

        var lote3 = Lote.builder()
            .pesoI(18.0)
            .pesoFina(10.0)
            .temperatura(4.0)
            .estado(EstadoLote.ABERTO)
            .abrigo(abrigo3)
            .build();

        loteRepository.saveAll(List.of(lote1, lote2, lote3));

        System.out.println("Dados de teste criados com sucesso.");
    }
}
