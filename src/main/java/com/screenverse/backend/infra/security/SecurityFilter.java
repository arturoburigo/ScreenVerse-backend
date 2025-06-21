package com.screenverse.backend.infra.security;

import com.screenverse.backend.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

   @Autowired
   private TokenService tokenService;

   @Autowired
   private UsersRepository usersRepository;

   @Override
   protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

      String tokenJWT = getToken(request);
      if (tokenJWT != null) {
         String subject = tokenService.getSubject(tokenJWT);
         // Aqui presumimos que `findByClerkUserId` retorna um objeto que implementa UserDetails
         Object user = usersRepository.findByClerkUserId(subject);

         UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(user, null,
                   null
              );

         SecurityContextHolder.getContext().setAuthentication(auth);
      }

      filterChain.doFilter(request, response);
   }

   private String getToken(HttpServletRequest request) {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
         return authHeader.substring(7);
      }
      return null;
   }
}
