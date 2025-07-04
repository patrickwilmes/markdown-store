package com.bitlake.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
  private final SecretKey key;
  private final long jwtExpiration;

  public JwtService(
    @Value("${jwt.secret}") String secret,
    @Value("${jwt.expiration:86400000}") long jwtExpiration
  ) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.jwtExpiration = jwtExpiration;
  }

  public String generateToken(String username) {
    return Jwts.builder()
      .subject(username)
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
      .signWith(key)
      .compact();
  }

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
