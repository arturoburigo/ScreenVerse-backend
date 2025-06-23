package com.screenverse.backend.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {

   private final SecurityFilter securityFilter;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
           .csrf(csrf -> csrf.disable())
           .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .authorizeHttpRequests(req -> {
              // Rotas públicas
              req.requestMatchers("/auth/**").permitAll();
              req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();

              // Todas as outras precisam autenticação
              req.anyRequest().authenticated();
           })
           .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
           .build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
   }
}