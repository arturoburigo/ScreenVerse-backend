package com.screenverse.backend.controller.auth;

import com.screenverse.backend.dto.auth.AuthResponseDTO;
import com.screenverse.backend.dto.auth.ClerkAuthRequestDTO;
import com.screenverse.backend.dto.auth.RefreshTokenRequestDTO;
import com.screenverse.backend.dto.auth.UserCheckResponseDTO;
import com.screenverse.backend.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

   private final AuthService authService;

   @PostMapping("/signup")
   public ResponseEntity<AuthResponseDTO> signUp(
        @Valid @RequestBody ClerkAuthRequestDTO request
   ) {
      AuthResponseDTO response = authService.signUp(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   @PostMapping("/clerk/signin")
   public ResponseEntity<AuthResponseDTO> signInWithClerk(
        @Valid @RequestBody ClerkAuthRequestDTO request
   ) {
      return ResponseEntity.ok(authService.authenticateOrCreateUser(request));
   }

   @PostMapping("/clerk/auth")
   public ResponseEntity<AuthResponseDTO> authenticateWithClerk(
        @Valid @RequestBody ClerkAuthRequestDTO request
   ) {
      return ResponseEntity.ok(authService.authenticateOrCreateUser(request));
   }


   @PostMapping("/refresh")
   public ResponseEntity<AuthResponseDTO> refreshToken(
        @Valid @RequestBody RefreshTokenRequestDTO request
   ) {
      return ResponseEntity.ok(authService.refreshToken(request));
   }

   @GetMapping("/check-user")
   public ResponseEntity<UserCheckResponseDTO> checkUser(
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String clerkUserId
   ) {
      UserCheckResponseDTO response = authService.checkUserExists(email, clerkUserId);
      return ResponseEntity.ok(response);
   }
}
