/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Username cannot be blank")
        @Size(min = 4, max = 20, message = "Username must be between 4-20 characters")
        String username,
    @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,
    @NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid")
        String email)
    implements UserCredentialsRequest {}
