/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.model.response;

import java.time.Instant;

public interface TokenResponse {
  String getJwtToken();

  Instant getExpiresAt();
}
