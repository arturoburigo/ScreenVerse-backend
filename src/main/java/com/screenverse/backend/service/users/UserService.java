package com.screenverse.backend.service.users;

import com.screenverse.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
   @Autowired
   private UsersRepository usersRepository;
}
