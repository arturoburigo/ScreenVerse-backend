package com.screenverse.backend.dto.users;

import com.screenverse.backend.domain.users.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
   private String clerkUserId;
   private String email;
   private String firstName;
   private String lastName;
   private String authProvider;

   public UserRequestDTO(Users user) {
      this.clerkUserId = user.getClerkUserId();
      this.email = user.getEmail();
      this.firstName = user.getFirstName();
      this.lastName = user.getLastName();
      this.authProvider = user.getAuthProvider();
   }
}