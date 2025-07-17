package com.lucipurr.sdk.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = "Username cannot be blank") String username,
    @NotBlank(message = "Password cannot be blank") String password,
    @NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid")
        String email) {}
