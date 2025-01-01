package com.example.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Schema(description = "Request object for user registration")
@Getter
@Setter
public class RegistrationRequest {

    @Schema(description = "User's email address", example = "user@example.com")
    @Email(message = "Invalid email")
    @NotBlank(message = "The email should not be empty")
    private String email;

    @Schema(description = "User's password", example = "password123", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Incorrect password size")
    @NotBlank(message = "The password must not be empty")
    private String password;

    @Schema(description = "User's birth date", example = "1990-01-01")
    @JsonProperty("birth_date")
    private Date birthDate;

}
