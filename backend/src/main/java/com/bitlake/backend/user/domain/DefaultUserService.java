package com.bitlake.backend.user.domain;

import com.bitlake.backend.user.User;
import com.bitlake.backend.user.UserRepository;
import com.bitlake.backend.user.UserService;
import org.springframework.stereotype.Service;

@Service
class DefaultUserService implements UserService {

  private final UserRepository userRepository;

  public DefaultUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getByUsername(String username) {
    return userRepository.getByUsername(username);
  }

  @Override
  public User register(User user) {
    return userRepository.register(user);
  }
}
