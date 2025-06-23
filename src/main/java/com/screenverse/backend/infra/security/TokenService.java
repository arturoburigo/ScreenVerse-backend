package com.screenverse.backend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.screenverse.backend.domain.users.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

   @Value("${api.security.token.secret}")
   private String secret;

   private static final String ISSUER = "API ScreenVerse";
   private static final long ACCESS_TOKEN_EXPIRATION = 7200000; // 2 horas em ms
   private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 dias em ms

   public String generateAccessToken(Users user) {
      try {
         Algorithm algorithm = Algorithm.HMAC256(secret);
         return JWT.create()
              .withIssuer(ISSUER)
              .withSubject(user.getEmail())
              .withClaim("userId", user.getId())
              .withClaim("clerkUserId", user.getClerkUserId())
              .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
              .sign(algorithm);
      } catch (JWTCreationException exception) {
         throw new RuntimeException("Erro ao gerar access token", exception);
      }
   }

   public String generateRefreshToken(Users user) {
      try {
         Algorithm algorithm = Algorithm.HMAC256(secret);
         return JWT.create()
              .withIssuer(ISSUER)
              .withSubject(user.getEmail())
              .withClaim("userId", user.getId())
              .withClaim("type", "refresh")
              .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
              .sign(algorithm);
      } catch (JWTCreationException exception) {
         throw new RuntimeException("Erro ao gerar refresh token", exception);
      }
   }

   public String validateToken(String token) {
      try {
         Algorithm algorithm = Algorithm.HMAC256(secret);
         JWTVerifier verifier = JWT.require(algorithm)
              .withIssuer(ISSUER)
              .build();

         DecodedJWT jwt = verifier.verify(token);
         return jwt.getSubject(); // retorna o email
      } catch (JWTVerificationException exception) {
         return null;
      }
   }

   public DecodedJWT getDecodedToken(String token) {
      try {
         Algorithm algorithm = Algorithm.HMAC256(secret);
         JWTVerifier verifier = JWT.require(algorithm)
              .withIssuer(ISSUER)
              .build();

         return verifier.verify(token);
      } catch (JWTVerificationException exception) {
         return null;
      }
   }
}