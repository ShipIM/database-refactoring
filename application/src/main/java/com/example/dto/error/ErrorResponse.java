package com.example.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response object containing error details")
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "Error code indicating the type of error", example = "code")
    private String code;

    @Schema(description = "Detailed error message", example = "message")
    private String message;

}
