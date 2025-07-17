package com.lucipurr.sdk.auth.service;

import com.lucipurr.sdk.auth.exception.EmailAlreadyExistsException;
import com.lucipurr.sdk.auth.exception.UserAlreadyExistsException;
import com.lucipurr.sdk.auth.exception.UserNotFoundException;
import com.lucipurr.sdk.auth.model.AuthRequest;
import com.lucipurr.sdk.auth.model.LoginResponse;
import com.lucipurr.sdk.auth.model.RegisterRequest;
import com.lucipurr.sdk.auth.model.SignupResponse;
import com.lucipurr.sdk.auth.model.UserDto;
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
public class AuthServiceImplNeo {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthServiceImplNeo(
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
    // 1. Validate & create user
    validations(request);

    User user =
        User.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .roles(Set.of("USER"))
            .build();

    User savedUser = userRepository.save(user);

    // 2. Auto-login after registration
    String jwtToken = jwtService.generateToken(savedUser);
    Instant expiresAt = jwtService.getExpirationTime();

    // 3. Return token directly
    return SignupResponse.of(savedUser, jwtToken, expiresAt);
  }

  private void validations(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException(request.email());
    }

    if (userRepository.existsByUsername(request.username())) {
      throw new UserAlreadyExistsException(request.username());
    }
  }

  public LoginResponse authenticate(AuthRequest request) {
    // 1. Verify credentials
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    // 2. Generate token
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
}
