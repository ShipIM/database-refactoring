package com.example.dto.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@Schema(description = "Pagination request for querying paginated data")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @Schema(description = "Page number for pagination (starts from 0)", example = "0")
    @Min(value = 0, message = "The page number must be a positive number")
    @Pattern(regexp = "\\d+", message = "The page number must be a number")
    @JsonProperty(value = "page_number")
    private String pageNumber;

    @Schema(description = "Page size for pagination (between 1 and 20)", example = "20")
    @Min(value = 1, message = "The page size should be >= 1")
    @Max(value = 20, message = "The page size should be <= 20")
    @Pattern(regexp = "\\d+", message = "The page size must be a number")
    @JsonProperty(value = "page_size")
    private String pageSize;

    public PageRequest formPageRequest() {
        return PageRequest.of(
                Integer.parseInt(Optional.ofNullable(pageNumber).orElse("0")),
                Integer.parseInt(Optional.ofNullable(pageSize).orElse("20"))
        );
    }

}
