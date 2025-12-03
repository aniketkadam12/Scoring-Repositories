package com.scoring.github.popularity_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class PopularityResponseDto {

    private int popularityScore;
    private String repositoryName;
    private String repositoryUrl;
    private int stars;
    private int forks;
    private Instant lastUpdatedAt;
    private Instant createdAt;
}
