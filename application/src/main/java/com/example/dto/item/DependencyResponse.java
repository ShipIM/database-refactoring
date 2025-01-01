package com.example.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Response object representing a dependency")
@Setter
@Getter
public class DependencyResponse {

    @Schema(description = "Name of the dependency", example = "Example Dependency")
    private String name;

    @Schema(description = "Unique identifier for the dependency", example = "12345")
    private Long id;

    @Schema(description = "Level of the dependency", example = "2")
    private Long level;

}
