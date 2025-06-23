package com.screenverse.backend.exception;

public class UserNotFoundException extends RuntimeException {
   public UserNotFoundException(String message) {
      super(message);
   }
}
