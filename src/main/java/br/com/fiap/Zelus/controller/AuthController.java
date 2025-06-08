package br.com.fiap.Zelus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.Zelus.model.Abrigo;
import br.com.fiap.Zelus.service.TokenService;

@RestController
public class AuthController {

    public record Token(String token, String email) {}
    public record Credentials(String email, String password) {}

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping("/login")
    public Token login(@RequestBody Credentials credentials) {
        var authentication = new UsernamePasswordAuthenticationToken(
            credentials.email(), credentials.password()
        );

        var abrigo = (Abrigo) authManager.authenticate(authentication).getPrincipal();
        return tokenService.createToken(abrigo);
    }
}
