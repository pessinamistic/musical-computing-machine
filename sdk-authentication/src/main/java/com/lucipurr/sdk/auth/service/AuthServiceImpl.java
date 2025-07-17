package com.lucipurr.sdk.auth.service;

import com.lucipurr.sdk.auth.model.AuthRequest;
import com.lucipurr.sdk.auth.model.AuthResponse;
import com.lucipurr.sdk.auth.model.RegisterRequest;
import com.lucipurr.sdk.core.model.User;
import com.lucipurr.sdk.core.repository.UserRepository;

import java.time.Instant;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService
{

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

  @Override
  public AuthResponse register(RegisterRequest request) {
    var user =
        User.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .email(request.email())
            .roles(Set.of("USER"))
            .build();

    userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
Instant expirationTime = jwtService.getExpirationTime();
return AuthResponse.of(
    jwtToken, user.getUsername(), expirationTime.getEpochSecond());
  }

  @Override
  public AuthResponse authenticate(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    var user = userRepository.findByUsername(request.username()).orElseThrow();

    var jwtToken = jwtService.generateToken(user);
    return AuthResponse.of(
        jwtToken, user.getUsername(), jwtService.getExpirationTime().getEpochSecond());
  }
}
