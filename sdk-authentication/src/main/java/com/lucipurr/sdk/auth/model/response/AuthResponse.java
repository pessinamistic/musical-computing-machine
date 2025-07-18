package com.lucipurr.sdk.auth.model.response;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, String username)
    implements TokenResponse {
  @Override
  public String getJwtToken() {
    return accessToken;
  }

  @Override
  public java.time.Instant getExpiresAt() {
    return java.time.Instant.ofEpochSecond(expiresIn);
  }

  public static AuthResponse of(String accessToken, String username, long expiresIn) {
    return new AuthResponse(accessToken, "Bearer", expiresIn, username);
  }
}
