package com.screenverse.backend.controller.users;

import com.screenverse.backend.dto.users.UserRequestDTO;
import com.screenverse.backend.dto.users.UserResponseDTO;
import com.screenverse.backend.service.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

   private final UserService userService;

   @GetMapping
   public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
      List<UserResponseDTO> users = userService.getAllUsers();
      return ResponseEntity.ok(users);
   }

   @GetMapping("/{id}")
   public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
      UserResponseDTO user = userService.getUserById(id);
      return ResponseEntity.ok(user);
   }

   @PostMapping
   public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
      UserResponseDTO createdUser = userService.createUser(userRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
   }

   @PutMapping("/{id}")
   public ResponseEntity<UserResponseDTO> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserRequestDTO userRequest) {
      UserResponseDTO updatedUser = userService.updateUser(id, userRequest);
      return ResponseEntity.ok(updatedUser);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
      userService.deleteUser(id);
      return ResponseEntity.noContent().build();
   }

}