package com.lucipurr.sdk.auth.model;

import com.lucipurr.sdk.core.model.User;
import java.time.Duration;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserDto(
    String id,
    String email,
    String username,
    Set<String> roles,
    boolean enabled,
    Duration createdAt,
    Duration updatedAt) {
  public static UserDto from(User user) {
    return UserDto.builder()
        .id(user.getId().toString())
        .email(user.getEmail())
        .username(user.getUsername())
        .roles(user.getRoles())
        .enabled(user.isEnabled())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}
