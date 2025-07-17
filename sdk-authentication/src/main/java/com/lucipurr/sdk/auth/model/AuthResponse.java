package com.lucipurr.sdk.auth.model;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, String username) {
  public static AuthResponse of(String accessToken, String username, long expiresIn) {
    return new AuthResponse(accessToken, "Bearer", expiresIn, username);
  }
}
