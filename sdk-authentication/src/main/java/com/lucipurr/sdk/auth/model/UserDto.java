/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.model;

import com.lucipurr.sdk.core.model.User;
import java.time.Instant;
import java.util.Set;

public record UserDto(
    String id,
    String email,
    String username,
    Set<String> roles,
    boolean enabled,
    Instant createdAt,
    Instant updatedAt) {

  public static UserDto from(User user) {
    return new UserDto(
        user.getId().toString(),
        user.getEmail(),
        user.getUsername(),
        user.getRoles(),
        user.isEnabled(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }
}
