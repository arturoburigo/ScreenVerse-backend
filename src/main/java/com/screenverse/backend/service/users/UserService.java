package com.screenverse.backend.service.users;

import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.dto.users.UserRequestDTO;
import com.screenverse.backend.dto.users.UserResponseDTO;
import com.screenverse.backend.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UsersRepository usersRepository;

   @Transactional(readOnly = true)
   public List<UserResponseDTO> getAllUsers() {
      return usersRepository.findAll()
           .stream()
           .map(UserResponseDTO::new)
           .collect(Collectors.toList());
   }

   @Transactional(readOnly = true)
   public UserResponseDTO getUserById(Long id) {
      Users user = usersRepository.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
      return new UserResponseDTO(user);
   }

   @Transactional
   public UserResponseDTO createUser(UserRequestDTO userRequest) {
      // Check if user with same email or clerkUserId already exists
      if (usersRepository.existsByEmail(userRequest.getEmail())) {
         throw new com.screenverse.backend.exception.ResourceAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists");
      }

      if (usersRepository.existsByClerkUserId(userRequest.getClerkUserId())) {
         throw new com.screenverse.backend.exception.ResourceAlreadyExistsException("User with clerk ID " + userRequest.getClerkUserId() + " already exists");
      }

      Users user = new Users();
      user.setClerkUserId(userRequest.getClerkUserId());
      user.setEmail(userRequest.getEmail());
      user.setFirstName(userRequest.getFirstName());
      user.setLastName(userRequest.getLastName());
      user.setAuthProvider(userRequest.getAuthProvider());

      Users savedUser = usersRepository.save(user);
      return new UserResponseDTO(savedUser);
   }

   @Transactional
   public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
      Users existingUser = usersRepository.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

      // Check for duplicate email
      usersRepository.findByEmail(userRequest.getEmail())
           .ifPresent(user -> {
              if (!user.getId().equals(id)) {
                 throw new com.screenverse.backend.exception.ResourceAlreadyExistsException("Email " + userRequest.getEmail() + " already in use");
              }
           });

      // Check for duplicate clerkUserId
      usersRepository.findByClerkUserId(userRequest.getClerkUserId())
           .ifPresent(user -> {
              if (!user.getId().equals(id)) {
                 throw new com.screenverse.backend.exception.ResourceAlreadyExistsException("Clerk user ID " + userRequest.getClerkUserId() + " already in use");
              }
           });

      existingUser.setClerkUserId(userRequest.getClerkUserId());
      existingUser.setEmail(userRequest.getEmail());
      existingUser.setFirstName(userRequest.getFirstName());
      existingUser.setLastName(userRequest.getLastName());
      existingUser.setAuthProvider(userRequest.getAuthProvider());

      Users updatedUser = usersRepository.save(existingUser);
      return new UserResponseDTO(updatedUser);
   }

   @Transactional
   public void deleteUser(Long id) {
      if (!usersRepository.existsById(id)) {
         throw new EntityNotFoundException("User not found with id: " + id);
      }
      usersRepository.deleteById(id);
   }
}
