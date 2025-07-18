package com.lucipurr.sdk.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    String username,
    @NotBlank(message = "Password cannot be blank") String password,
    @Email(message = "Email should be valid") String email)
    implements UserCredentialsRequest {}
