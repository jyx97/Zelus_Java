package br.com.fiap.zelus.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fiap.zelus.controller.AuthController.Credentials;
import br.com.fiap.zelus.controller.AuthController.Token;
import br.com.fiap.zelus.model.Abrigo;

@Service
public class TokenService {

    Instant expiresAt = LocalDateTime.now().plusMinutes(120).toInstant(ZoneOffset.ofHours(-3));
    Algorithm algorithm = Algorithm.HMAC256("secret");

    public Token createToken(User user){
        var jwt = JWT.create()
            .withSubject(user.getId().toString())
            .withClaim("email", user.getEmail())
            .withClaim("role", user.getRole().toString())
            .withExpiresAt(expiresAt)
            .sign(algorithm);

        return new Token(jwt, user.getEmail());

    }

    public User getUserFromToken(String token) {
        var verifiedToken = JWT.require(algorithm).build().verify(token);
        return User.builder()
                .id(Long.valueOf(verifiedToken.getSubject()))
                .email(verifiedToken.getClaim("email").toString())
                .role(UserRole.valueOf( verifiedToken.getClaim("role").asString() ))
                .build();

    }
    
}