package com.bitlake.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userService;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userService) {
    this.jwtService = jwtService;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {

    String path = request.getRequestURI();

    if (path.equals("/user/register") || path.equals("/user/login")) {
      filterChain.doFilter(request, response);
      return;
    }

    if (request.getCookies() == null) {
      filterChain.doFilter(request, response);
      return;
    }

    Optional<Cookie> jwtCookie =
      Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt"))
        .findFirst();

    if (jwtCookie.isPresent()) {
      final String jwt = jwtCookie.get().getValue();
      final String username = jwtService.extractUsername(jwt);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        final UserDetails user = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
