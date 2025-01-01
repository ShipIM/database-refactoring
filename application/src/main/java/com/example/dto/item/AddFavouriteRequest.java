package com.example.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Request object for adding an item to the favorites list")
@Getter
@Setter
public class AddFavouriteRequest {

    @Schema(description = "Unique identifier of the item to be added to favorites",
            example = "123456789", pattern = "^(?!0+$)\\d{1,19}$")
    @NotBlank(message = "The item ID must not be empty")
    @Pattern(regexp = "^(?!0+$)\\d{1,19}$",
            message = "The item ID must be a positive number of type long")
    @JsonProperty("item_id")
    private String itemId;

}
