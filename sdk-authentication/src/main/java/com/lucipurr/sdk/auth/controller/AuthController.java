package com.lucipurr.sdk.auth.controller;

import com.lucipurr.sdk.auth.model.AuthRequest;
import com.lucipurr.sdk.auth.model.AuthResponse;
import com.lucipurr.sdk.auth.model.LoginResponse;
import com.lucipurr.sdk.auth.model.RegisterRequest;
import com.lucipurr.sdk.auth.model.SignupResponse;
import com.lucipurr.sdk.auth.service.AuthService;
import com.lucipurr.sdk.auth.service.AuthServiceImplNeo;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final AuthServiceImplNeo authServiceImplNeo;

  public AuthController(AuthService authService, AuthServiceImplNeo authServiceImplNeo) {
    this.authService = authService;
    this.authServiceImplNeo = authServiceImplNeo;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
    return ResponseEntity.ok(authService.authenticate(request));
  }

  @PostMapping("/registerNeo")
  public ResponseEntity<SignupResponse> register2(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authServiceImplNeo.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody AuthRequest request) {
    return ResponseEntity.ok(authServiceImplNeo.authenticate(request));
  }
}
