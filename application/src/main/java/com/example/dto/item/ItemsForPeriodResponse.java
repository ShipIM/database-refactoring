package com.example.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "Response object representing the details of items for a specific day")
@Getter
@Setter
public class ItemsForPeriodResponse {

    @Schema(description = "The specific day of the item data", example = "2024-01-01")
    private LocalDate day;

    @Schema(description = "The maximum cost to buy the item on this day", example = "1500")
    @JsonProperty("max_cost_buy")
    private Long maxCostBuy;

    @Schema(description = "The quantity of items available on this day", example = "10")
    private Long quantity;

}
