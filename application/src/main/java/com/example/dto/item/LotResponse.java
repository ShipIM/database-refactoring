package com.example.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "Response object representing the details of a lot")
@Getter
@Setter
public class LotResponse {

    @Schema(description = "Unique identifier for the lot", example = "1001")
    @JsonProperty("lot_id")
    private long id;

    @Schema(description = "Login of the vendor who created the lot", example = "vendor123")
    @JsonProperty("vendor")
    private String login;

    @Schema(description = "Current cost of the lot", example = "5000")
    @JsonProperty("cost_current")
    private long current;

    @Schema(description = "Buy cost of the lot", example = "4000")
    @JsonProperty("cost_buy")
    private long buy;

    @Schema(description = "End time of the lot auction", example = "2024-12-31T23:59:59")
    @JsonProperty("time_end")
    private LocalDateTime end;

}
