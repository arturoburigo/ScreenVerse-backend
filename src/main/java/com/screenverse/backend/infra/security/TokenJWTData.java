package com.screenverse.backend.infra.security;

public class TokenJWTData {
   private final String tokenJWT;

   public TokenJWTData(String tokenJWT) {
      this.tokenJWT = tokenJWT;
   }

   public String getTokenJWT() {
      return tokenJWT;
   }
}
