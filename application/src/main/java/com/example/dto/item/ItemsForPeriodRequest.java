package com.example.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "Request object for fetching items within a specific period")
@Setter
@Getter
public class ItemsForPeriodRequest {

    @Schema(description = "Start date of the period", example = "2024-01-01")
    private LocalDate start;

    @Schema(description = "End date of the period", example = "2024-12-31")
    private LocalDate end;

    @Schema(description = "Unique identifier of the item",
            example = "123456789",
            pattern = "^(?!0+$)\\d{1,19}$")
    @NotBlank(message = "The item ID must not be empty")
    @Pattern(regexp = "^(?!0+$)\\d{1,19}$", message = "The item ID must be a positive number of type long")
    @JsonProperty("item_id")
    private String itemId;

}
