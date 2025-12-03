package com.scoring.github.popularity_service.scoringstrategy;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;

public interface ScoringStrategy {

    String getName();

    int calculateScore(GithubSearchResponseDto.Item repo);
}
