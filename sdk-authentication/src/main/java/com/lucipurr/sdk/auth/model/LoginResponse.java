package com.lucipurr.sdk.auth.model;

import java.time.Instant;
import java.util.Set;

public record LoginResponse(
    String jwtToken, UserDto user, Instant expiresAt, Set<String> authorities) {}
