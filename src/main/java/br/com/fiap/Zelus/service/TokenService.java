package br.com.fiap.Zelus.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fiap.Zelus.controller.AuthController.Token;
import br.com.fiap.Zelus.model.Abrigo;
import br.com.fiap.Zelus.model.Abrigo.StatusAbrigo;

@Service
public class TokenService {

    private final Instant expiresAt = LocalDateTime.now()
        .plusMinutes(120)
        .toInstant(ZoneOffset.ofHours(-3));

    private final Algorithm algorithm = Algorithm.HMAC256("secret");

    public Token createToken(Abrigo abrigo) {
        var jwt = JWT.create()
            .withSubject(abrigo.getId().toString())
            .withClaim("email", abrigo.getEmail())
            .withClaim("status", abrigo.getStatus().name())
            .withExpiresAt(expiresAt)
            .sign(algorithm);

        return new Token(jwt, abrigo.getEmail());
    }

    public Abrigo getUserFromToken(String token) {
        var verifiedToken = JWT.require(algorithm)
            .build()
            .verify(token);

        return Abrigo.builder()
            .id(Long.valueOf(verifiedToken.getSubject()))
            .email(verifiedToken.getClaim("email").asString())
            .status(StatusAbrigo.valueOf(verifiedToken.getClaim("status").asString()))
            .build();
    }
}
