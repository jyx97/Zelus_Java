package br.com.fiap.Zelus.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.fiap.Zelus.model.Abrigo;

public interface AbrigoRepository extends JpaRepository <Abrigo, Long>, JpaSpecificationExecutor<Abrigo>{

    Optional<Abrigo> findByEmail(String email);
    
}