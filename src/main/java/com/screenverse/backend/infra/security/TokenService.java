package com.screenverse.backend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

   @Value("${api.security.token.secret}")
   private String secret;

   // Substitua pelo seu User entity correto
   // Exemplo: se seu User está em com.screenverse.backend.model.User
   public String generateToken(com.screenverse.backend.model.User user) {
      try {
         Algorithm algorithm = Algorithm.HMAC256(secret);
         return JWT.create()
              .withIssuer("API ScreenVerse")
              .withSubject(user.getUsername()) // ou user.getLogin() dependendo do seu método
              .withExpiresAt(Date.from(dateExpiration()))
              .sign(algorithm);
      } catch (JWTCreationException exception) {
         throw new RuntimeException("Erro ao gerar token", exception);
      }
   }

   public String getSubject(String token) {
      try {
         Algorithm algorithm = Algorithm.HMAC256(secret);
         return JWT.require(algorithm)
              .withIssuer("API ScreenVerse")
              .build()
              .verify(token)
              .getSubject();
      } catch (JWTVerificationException exception) {
         throw new RuntimeException("Token inválido", exception);
      }
   }

   private Instant dateExpiration() {
      return LocalDateTime.now()
           .plusHours(2)
           .toInstant(ZoneOffset.of("-03:00"));
   }
}