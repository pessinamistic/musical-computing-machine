/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.controller;

import com.lucipurr.sdk.auth.model.request.AuthRequest;
import com.lucipurr.sdk.auth.model.request.RegisterRequest;
import com.lucipurr.sdk.auth.model.response.TokenResponse;
import com.lucipurr.sdk.auth.service.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthServiceImpl authServiceImpl;

  public AuthController(AuthServiceImpl authServiceImpl) {
    this.authServiceImpl = authServiceImpl;
  }

  @PostMapping("/register")
  public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
    // Exception handling is now managed globally
    return ResponseEntity.ok(authServiceImpl.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
    // Exception handling is now managed globally
    return ResponseEntity.ok(authServiceImpl.authenticate(request));
  }
}
