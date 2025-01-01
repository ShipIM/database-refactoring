package com.example.controller;

import com.example.dto.authentication.AuthenticationRequest;
import com.example.dto.authentication.AuthenticationResponse;
import com.example.dto.authentication.RegistrationRequest;
import com.example.dto.error.ErrorResponse;
import com.example.mapper.UserMapper;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "users", description = "The controller for registration and authorization")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService service;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    @Operation(description = "Register a new user", summary = "Register a new user", tags = {"users"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Validation errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public AuthenticationResponse register(
            @RequestBody
            @Valid
            RegistrationRequest request) {
        var user = userMapper.mapToUser(request);
        var registration = service.register(user);
        return new AuthenticationResponse(registration.getValue(), registration.getKey().getEmail());
    }

    @PostMapping("/authentication")
    @Operation(description = "Authorize an existing user", summary = "Authorize an existing user", tags = {"users"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authorized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Validation errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public AuthenticationResponse authenticate(
            @RequestBody
            @Valid
            AuthenticationRequest request) {
        var user = userMapper.mapToUser(request);
        var authentication = service.authenticate(user);
        return new AuthenticationResponse(authentication.getValue(), authentication.getKey().getEmail());
    }
}
