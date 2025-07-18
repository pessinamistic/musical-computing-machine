package com.lucipurr.sdk.auth.service;

import com.lucipurr.sdk.auth.exception.EmailAlreadyExistsException;
import com.lucipurr.sdk.auth.exception.UserAlreadyExistsException;
import com.lucipurr.sdk.auth.exception.UserNotFoundException;
import com.lucipurr.sdk.auth.model.UserDto;
import com.lucipurr.sdk.auth.model.request.AuthRequest;
import com.lucipurr.sdk.auth.model.request.RegisterRequest;
import com.lucipurr.sdk.auth.model.response.LoginResponse;
import com.lucipurr.sdk.auth.model.response.SignupResponse;
import com.lucipurr.sdk.core.model.User;
import com.lucipurr.sdk.core.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthServiceImpl(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @Transactional
  public SignupResponse register(RegisterRequest request) {
    validations(request);

    // Normalize email to lowercase
    String normalizedEmail = request.email().trim().toLowerCase();

    User.UserBuilder userBuilder = User.builder()
        .username(request.username())
        .email(normalizedEmail)
        .password(passwordEncoder.encode(request.password()))
        .roles(Set.of("USER"));

    // Optionally set audit fields if present
    // userBuilder.createdAt(Instant.now());

    User user = userBuilder.build();
    User savedUser = userRepository.save(user);

    String jwtToken = jwtService.generateToken(savedUser);
    Instant expiresAt = jwtService.getExpirationTime();

    return SignupResponse.of(savedUser, jwtToken, expiresAt);
  }

  public LoginResponse authenticate(AuthRequest request) {
    // Make sure your UserDetailsService loads users by email, not username!
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    } catch (Exception ex) {
      // Propagate authentication failure as a custom exception
      throw new com.lucipurr.sdk.auth.exception.AuthException("Invalid credentials");
    }

    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException(request.email()));

    String jwtToken = jwtService.generateToken(user);

    return new LoginResponse(
        jwtToken,
        UserDto.from(user),
        jwtService.getExpirationTime(),
        user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet()));
  }

  private void validations(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException(request.email());
    }

    if (userRepository.existsByUsername(request.username())) {
      throw new UserAlreadyExistsException(request.username());
    }
  }
}
