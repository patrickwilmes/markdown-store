package com.bitlake.backend.user;

public record User(
  String username,
  String hashedPassword
) {
}
