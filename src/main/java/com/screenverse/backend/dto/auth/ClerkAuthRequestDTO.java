package com.screenverse.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClerkAuthRequestDTO {
   @NotBlank(message = "ClerkUserId is required")
   private String clerkUserId;

   @NotBlank(message = "Email is required")
   @Email(message = "Invalid email format")
   private String email;

   private String firstName;
   private String lastName;

   @NotBlank(message = "Auth provider is required")
   private String authProvider; // "google" ou "github"
}