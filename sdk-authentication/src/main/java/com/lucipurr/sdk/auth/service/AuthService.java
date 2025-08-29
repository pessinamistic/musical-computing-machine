/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.service;

import com.lucipurr.sdk.auth.model.request.AuthRequest;
import com.lucipurr.sdk.auth.model.request.RegisterRequest;
import com.lucipurr.sdk.auth.model.response.TokenResponse;

public interface AuthService {
  TokenResponse register(RegisterRequest request);

  TokenResponse authenticate(AuthRequest request);
}
