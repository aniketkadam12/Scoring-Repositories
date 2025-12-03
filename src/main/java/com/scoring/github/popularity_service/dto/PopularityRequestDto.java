package com.scoring.github.popularity_service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class PopularityRequestDto {

    @Pattern(regexp = "^[A-Za-z0-9_+\\-#.]*$", message = "Language contains invalid characters")
    private String language;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdAfter;

    @NotBlank(message = "Algorithm cannot be empty")
    private String algorithm;

    @Min(value = 1, message = "Limit must be >= 1")
    @Max(value = 1000, message = "Limit must be <= 1000")
    private int limit = 200;

}
