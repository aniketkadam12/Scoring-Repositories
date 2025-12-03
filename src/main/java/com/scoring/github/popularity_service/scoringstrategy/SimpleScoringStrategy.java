package com.scoring.github.popularity_service.scoringstrategy;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class SimpleScoringStrategy implements ScoringStrategy {

    @Override
    public String getName() {
        return "simple";
    }

    @Override
    public int calculateScore(GithubSearchResponseDto.Item repo) {
        int stars = repo.getStargazersCount();
        int forks = repo.getForks();
        Instant updatedAt = repo.getUpdatedAt() != null ? repo.getUpdatedAt() : repo.getPushedAt();

        return (stars * 2) + forks + calculateRecencyWeight(updatedAt);
    }

    private int calculateRecencyWeight(Instant lastUpdatedAt) {
        if (lastUpdatedAt == null) return 0;

        long days = Duration.between(lastUpdatedAt, Instant.now()).toDays();

        if (days <= 30) return 100;
        if (days <= 90) return 50;
        return 10;
    }
}
