package com.lucipurr.sdk.auth.model;

import com.lucipurr.sdk.core.model.User;
import java.time.Instant;

public record SignupResponse(
    boolean success,
    String message,
    String jwtToken, // Auto-login token
    UserDto user,
    Instant expiresAt) {
  public static SignupResponse of(User user, String jwtToken, Instant expiresAt) {
    return new SignupResponse(
        true, "Registration successful", jwtToken, UserDto.from(user), expiresAt);
  }
}
