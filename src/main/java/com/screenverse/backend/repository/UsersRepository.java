package com.screenverse.backend.repository;

import com.screenverse.backend.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
