package com.screenverse.backend.infra.security;

import com.screenverse.backend.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

   private final TokenService tokenService;
   private final UsersRepository usersRepository;

   @Override
   protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
      var authHeader = request.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
         var token = authHeader.substring(7);
         var subject = tokenService.validateToken(token);

         if (subject != null) {
            var user = usersRepository.findByEmail(subject);

            if (user.isPresent()) {
               var authentication = new UsernamePasswordAuthenticationToken(
                    user.get(),
                    null,
                    new ArrayList<>()
               );
               SecurityContextHolder.getContext().setAuthentication(authentication);
            }
         }
      }

      filterChain.doFilter(request, response);
   }
}