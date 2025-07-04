package com.bitlake.backend.user;

import com.bitlake.backend.exception.NotFoundException;

public interface UserService {
  User getByUsername(String username) throws NotFoundException;
  User register(User user);
}
