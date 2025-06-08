package br.com.fiap.Zelus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.Zelus.model.Abrigo;
import br.com.fiap.Zelus.model.Abrigo.StatusAbrigo;
import br.com.fiap.Zelus.repository.AbrigoRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Abrigo abrigo = abrigoRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Abrigo com email '" + email + "' não encontrado"));

        if (abrigo.getStatus() != StatusAbrigo.ATIVO) {
            throw new UsernameNotFoundException("Abrigo com status '" + abrigo.getStatus() + "' não pode acessar o sistema.");
        }

        return abrigo;
    }
}
