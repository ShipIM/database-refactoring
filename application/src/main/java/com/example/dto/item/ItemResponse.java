package com.example.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response object representing an item")
@Getter
@Setter
public class ItemResponse {

    @Schema(description = "Unique identifier for the item", example = "123")
    private Integer id;

    @Schema(description = "Name of the item", example = "Laptop")
    private String name;

    @Schema(description = "Properties of the item, typically a JSON string or description",
            example = "{\"color\":\"black\",\"size\":\"15 inch\"}")
    private String properties;

    @Schema(description = "Indicates whether the item is marked as a favorite", example = "true")
    @JsonProperty("is_favourite")
    private Boolean isFavourite;

}
