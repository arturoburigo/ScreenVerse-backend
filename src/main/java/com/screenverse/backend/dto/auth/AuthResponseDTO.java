package com.screenverse.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
   private String accessToken;
   private String refreshToken;
   private Long userId;
   private String email;
   private String firstName;
   private String lastName;
   private boolean isNewUser; // Indica se foi criado agora
}