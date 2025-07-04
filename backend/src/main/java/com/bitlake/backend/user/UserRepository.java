package com.bitlake.backend.user;

public interface UserRepository {
  User getByUsername(String username);
  User register(User user);
}
