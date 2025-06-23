package infra;


import com.screenverse.backend.exception.ResourceAlreadyExistsException;
import com.screenverse.backend.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(ResourceAlreadyExistsException.class)
   public ResponseEntity<Map<String, Object>> handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
      Map<String, Object> response = new HashMap<>();
      response.put("timestamp", LocalDateTime.now());
      response.put("message", e.getMessage());
      response.put("status", HttpStatus.CONFLICT.value());
      response.put("error", "USER_ALREADY_EXISTS");
      response.put("action", "REDIRECT_TO_SIGNIN");

      return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
   }

   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException e) {
      Map<String, Object> response = new HashMap<>();
      response.put("timestamp", LocalDateTime.now());
      response.put("message", e.getMessage());
      response.put("status", HttpStatus.NOT_FOUND.value());
      response.put("error", "USER_NOT_FOUND");
      response.put("action", "REDIRECT_TO_SIGNUP");

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
      Map<String, Object> response = new HashMap<>();
      response.put("timestamp", LocalDateTime.now());
      response.put("message", e.getMessage());
      response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
   }
}
