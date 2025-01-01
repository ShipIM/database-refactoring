package com.example.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response object containing the authentication token and user email")
@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse {

    @Schema(description = "JWT authentication token", example = "eyJhbGciOiJ...")
    private String token;

    @Schema(description = "Email address of the authenticated user", example = "user@example.com")
    private String email;

}
