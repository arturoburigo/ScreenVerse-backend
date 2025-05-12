package com.screenverse.backend.repository;

import com.screenverse.backend.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
   Optional<Users> findByEmail(String email);
   Optional<Users> findByClerkUserId(String clerkUserId);
   boolean existsByEmail(String email);
   boolean existsByClerkUserId(String clerkUserId);
}