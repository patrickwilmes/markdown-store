package com.bitlake.backend.user.api.rest;

import com.bitlake.backend.exception.NotFoundException;
import com.bitlake.backend.security.JwtService;
import com.bitlake.backend.user.User;
import com.bitlake.backend.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDtoMapper userDtoMapper = Mappers.getMapper(UserDtoMapper.class);

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(PasswordEncoder passwordEncoder,
    UserService userService,
    JwtService jwtService,
    AuthenticationManager authenticationManager) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody UserDto userDto) {
    final User user = new User(userDto.username(), passwordEncoder.encode(userDto.password()));
    final User createdUser = userService.register(user);
    final URI userLocation =
      ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/{username}")
        .buildAndExpand(createdUser.username()).toUri();
    return ResponseEntity.created(userLocation).build();
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserDto> getUser(@PathVariable String username) {
    try {
      final User user = userService.getByUsername(username);
      final UserDto userDto = userDtoMapper.toDto(user);
      return ResponseEntity.ok(userDto);
    } catch (NotFoundException e) {
      logger.warn("User not found: {}", username);
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/login")
  public ResponseEntity<Void> login(@Valid @RequestBody UserDto userDto,
    HttpServletResponse response) {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password())
      );

      final User user = userService.getByUsername(userDto.username());
      if (passwordEncoder.matches(userDto.password(), user.hashedPassword())) {
        final String jwt = jwtService.generateToken(user.username());

        ResponseCookie cookie =
          ResponseCookie.from("jwt", jwt)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(24 * 60 * 60)
            .sameSite("Lax")
            .domain("localhost")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
      }
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/auth-me")
  public ResponseEntity<Void> authMe() {
    return ResponseEntity.ok().build();
  }
}
