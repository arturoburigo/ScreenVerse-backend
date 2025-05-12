package com.screenverse.backend.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

   @NotBlank(message = "Clerk user ID is required")
   private String clerkUserId;

   @NotBlank(message = "Email is required")
   @Email(message = "Email should be valid")
   private String email;

   private String firstName;
   private String lastName;
   private String authProvider;
}