/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.model.response;

import com.lucipurr.sdk.auth.model.UserDto;
import java.time.Instant;
import java.util.Set;

public record LoginResponse(
    String jwtToken, UserDto user, Instant expiresAt, Set<String> authorities)
    implements TokenResponse {
  @Override
  public String getJwtToken() {
    return jwtToken;
  }

  @Override
  public Instant getExpiresAt() {
    return expiresAt;
  }
}
