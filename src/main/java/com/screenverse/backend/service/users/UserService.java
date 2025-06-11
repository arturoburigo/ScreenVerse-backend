package com.screenverse.backend.service.users;

import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.dto.users.UserRequestDTO;
import com.screenverse.backend.dto.users.UserResponseDTO;
import com.screenverse.backend.repository.UsersRepository;
import com.screenverse.backend.exception.ResourceAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UsersRepository usersRepository;

   @Transactional(readOnly = true)
   public List<UserResponseDTO> getAllUsers() {
      List<Users> users = usersRepository.findAll();
      List<UserResponseDTO> userResponseDTOs = new ArrayList<>();

      for (Users user : users) {
         userResponseDTOs.add(convertToDTO(user));
      }

      return userResponseDTOs;
   }

   @Transactional(readOnly = true)
   public UserResponseDTO getUserById(Long id) {
      Users user = findUserById(id);
      return convertToDTO(user);
   }

   @Transactional
   public UserResponseDTO createUser(UserRequestDTO userRequest) {
      // Check if user with same email or clerkUserId already exists
      checkEmailAndClerkIdUniqueness(null, userRequest.getEmail(), userRequest.getClerkUserId());

      Users user = new Users();
      updateUserFields(user, userRequest);

      Users savedUser = usersRepository.save(user);
      return convertToDTO(savedUser);
   }

   @Transactional
   public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
      Users existingUser = findUserById(id);

      // Check for duplicate email and clerkUserId
      checkEmailAndClerkIdUniqueness(id, userRequest.getEmail(), userRequest.getClerkUserId());

      updateUserFields(existingUser, userRequest);

      Users updatedUser = usersRepository.save(existingUser);
      return convertToDTO(updatedUser);
   }

   @Transactional
   public void deleteUser(Long id) {
      if (!usersRepository.existsById(id)) {
         throw new EntityNotFoundException("User not found with id: " + id);
      }
      usersRepository.deleteById(id);
   }

   // Helper methods to reduce code duplication
   private Users findUserById(Long id) {
      return usersRepository.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
   }

   private UserResponseDTO convertToDTO(Users user) {
      return new UserResponseDTO(user);
   }

   private void updateUserFields(Users user, UserRequestDTO userRequest) {
      user.setClerkUserId(userRequest.getClerkUserId());
      user.setEmail(userRequest.getEmail());
      user.setFirstName(userRequest.getFirstName());
      user.setLastName(userRequest.getLastName());
      user.setAuthProvider(userRequest.getAuthProvider());
   }

   private void checkEmailAndClerkIdUniqueness(Long id, String email, String clerkUserId) {
      // Check for duplicate email
      usersRepository.findByEmail(email)
           .ifPresent(user -> {
              if (!user.getId().equals(id)) {
                 throw new ResourceAlreadyExistsException("Email " + email + " already in use");
              }
           });

      // Check for duplicate clerkUserId
      usersRepository.findByClerkUserId(clerkUserId)
           .ifPresent(user -> {
              if (!user.getId().equals(id)) {
                 throw new ResourceAlreadyExistsException("Clerk user ID " + clerkUserId + " already in use");
              }
           });
   }
}