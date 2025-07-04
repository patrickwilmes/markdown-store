package com.bitlake.backend.configuration;

import com.bitlake.backend.security.JwtAuthenticationFilter;
import com.bitlake.backend.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final CorsConfigurationSource corsConfigurationSource;

  public SecurityConfig(
    JwtService jwtService,
    UserDetailsService userDetailsService,
    PasswordEncoder passwordEncoder,
    CorsConfigurationSource corsConfigurationSource
  ) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.corsConfigurationSource = corsConfigurationSource;
  }


  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(authProvider);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .cors(cors -> cors.configurationSource(corsConfigurationSource))
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/user/register", "/user/login").permitAll()
        .anyRequest().authenticated()
      )
      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService),
        UsernamePasswordAuthenticationFilter.class)
      .build();
  }

}
