package com.example.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response object representing the details of an item")
@Getter
@Setter
public class ItemViewResponse {

    @Schema(description = "Unique identifier for the item", example = "123")
    private Integer id;

    @Schema(description = "Name of the item", example = "Smartphone")
    private String name;

    @Schema(description = "Properties of the item, typically a JSON string or description",
            example = "{\"color\":\"black\",\"model\":\"XYZ\"}")
    private String properties;

}
