package com.bitlake.backend.user.api.rest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDto(
  @NotNull
  @Size(min = 3, max = 50)
  String username,
  @NotNull
  @Size(min = 8, max = 100)
  @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
  @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
  @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
  String password
) {
}
