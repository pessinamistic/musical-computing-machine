package com.lucipurr.sdk.auth.service;

import com.lucipurr.sdk.auth.model.AuthRequest;
import com.lucipurr.sdk.auth.model.AuthResponse;
import com.lucipurr.sdk.auth.model.RegisterRequest;

public interface AuthService {
  AuthResponse register(RegisterRequest request);
  AuthResponse authenticate(AuthRequest request);
}
