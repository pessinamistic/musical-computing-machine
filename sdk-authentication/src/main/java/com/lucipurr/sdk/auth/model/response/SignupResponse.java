/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.model.response;

import com.lucipurr.sdk.auth.model.UserDto;
import com.lucipurr.sdk.core.model.User;
import java.time.Instant;

public record SignupResponse(
    boolean success, String message, String jwtToken, UserDto user, Instant expiresAt)
    implements TokenResponse {
  @Override
  public String getJwtToken() {
    return jwtToken;
  }

  @Override
  public Instant getExpiresAt() {
    return expiresAt;
  }

  public static SignupResponse of(User user, String jwtToken, Instant expiresAt) {
    return new SignupResponse(
        true, "Registration successful", jwtToken, UserDto.from(user), expiresAt);
  }
}
