package com.example.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request object for user authentication")
@Getter
@Setter
public class AuthenticationRequest {

    @Schema(description = "User email address", example = "user@example.com")
    @Email(message = "Invalid email")
    private String email;

    @Schema(description = "User password", example = "password123", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Incorrect password size")
    @NotBlank(message = "The password must not be empty")
    private String password;

}
