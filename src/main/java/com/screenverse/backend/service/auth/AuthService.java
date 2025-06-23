package com.screenverse.backend.service.auth;

import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.dto.auth.*;
import com.screenverse.backend.exception.ResourceAlreadyExistsException;
import com.screenverse.backend.exception.UserNotFoundException;
import com.screenverse.backend.infra.security.TokenService;
import com.screenverse.backend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

   private final UsersRepository usersRepository;
   private final TokenService tokenService;

   // Signup - Cria novo usuário
   @Transactional
   public AuthResponseDTO signUp(ClerkAuthRequestDTO request) {
      // Verifica se usuário já existe
      if (usersRepository.existsByEmail(request.getEmail())) {
         throw new ResourceAlreadyExistsException(
              "User already exists with email: " + request.getEmail() +
                   ". Please use signin instead."
         );
      }

      if (usersRepository.existsByClerkUserId(request.getClerkUserId())) {
         throw new ResourceAlreadyExistsException(
              "User already exists with this Clerk ID. Please use signin instead."
         );
      }

      // Cria novo usuário
      Users newUser = new Users();
      newUser.setClerkUserId(request.getClerkUserId());
      newUser.setEmail(request.getEmail());
      newUser.setFirstName(request.getFirstName());
      newUser.setLastName(request.getLastName());
      newUser.setAuthProvider(request.getAuthProvider());

      Users savedUser = usersRepository.save(newUser);

      return generateAuthResponse(savedUser);
   }

   // Signin - Login de usuário existente
   public AuthResponseDTO signIn(ClerkSignInRequestDTO request) {
      Users user;

      // Busca por clerkUserId ou email
      if (request.getClerkUserId() != null) {
         user = usersRepository.findByClerkUserId(request.getClerkUserId())
              .orElseThrow(() -> new UserNotFoundException(
                   "User not found. Please signup first."
              ));
      }
      else {
         throw new IllegalArgumentException("Email or ClerkUserId is required");
      }

      return generateAuthResponse(user);
   }

   // Método unificado - Tenta signin primeiro, se não existir cria
   @Transactional
   public AuthResponseDTO authenticateOrCreateUser(ClerkAuthRequestDTO request) {
      // Busca usuário existente
      Users user = usersRepository.findByClerkUserId(request.getClerkUserId())
           .or(() -> usersRepository.findByEmail(request.getEmail()))
           .orElse(null);

      if (user == null) {
         // Cria novo usuário
         user = new Users();
         user.setClerkUserId(request.getClerkUserId());
         user.setEmail(request.getEmail());
         user.setFirstName(request.getFirstName());
         user.setLastName(request.getLastName());
         user.setAuthProvider(request.getAuthProvider());

         user = usersRepository.save(user);
      } else {
         // Atualiza dados do usuário se necessário
         boolean updated = false;

         if (request.getFirstName() != null && !request.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(request.getFirstName());
            updated = true;
         }

         if (request.getLastName() != null && !request.getLastName().equals(user.getLastName())) {
            user.setLastName(request.getLastName());
            updated = true;
         }

         if (updated) {
            user = usersRepository.save(user);
         }
      }

      return generateAuthResponse(user);
   }

   // Verificar se usuário existe
   public UserCheckResponseDTO checkUserExists(String email, String clerkUserId) {
      boolean exists = false;
      String message = "User not found";

      if (clerkUserId != null && usersRepository.existsByClerkUserId(clerkUserId)) {
         exists = true;
         message = "User found by Clerk ID";
      } else if (email != null && usersRepository.existsByEmail(email)) {
         exists = true;
         message = "User found by email";
      }

      return UserCheckResponseDTO.builder()
           .exists(exists)
           .message(message)
           .build();
   }

   // Refresh token
   public AuthResponseDTO refreshToken(RefreshTokenRequestDTO request) {
      var decodedToken = tokenService.getDecodedToken(request.getRefreshToken());

      if (decodedToken == null || !"refresh".equals(decodedToken.getClaim("type").asString())) {
         throw new RuntimeException("Invalid refresh token");
      }

      String email = decodedToken.getSubject();
      var user = usersRepository.findByEmail(email)
           .orElseThrow(() -> new RuntimeException("User not found"));

      return generateAuthResponse(user);
   }

   // Método auxiliar para gerar resposta com tokens
   private AuthResponseDTO generateAuthResponse(Users user) {
      String accessToken = tokenService.generateAccessToken(user);
      String refreshToken = tokenService.generateRefreshToken(user);

      return AuthResponseDTO.builder()
           .accessToken(accessToken)
           .refreshToken(refreshToken)
           .userId(user.getId())
           .email(user.getEmail())
           .firstName(user.getFirstName())
           .lastName(user.getLastName())
           .isNewUser(false) // Você pode adicionar lógica para detectar se é novo
           .build();
   }
}