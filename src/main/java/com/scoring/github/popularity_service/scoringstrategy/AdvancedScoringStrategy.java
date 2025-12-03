package com.scoring.github.popularity_service.scoringstrategy;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class AdvancedScoringStrategy implements ScoringStrategy {

    @Override
    public String getName() {
        return "advanced";
    }

    @Override
    public int calculateScore(GithubSearchResponseDto.Item repo) {
        int stars = repo.getStargazersCount();
        int forks = repo.getForks();
        Instant createdAt = repo.getCreatedAt();
        Instant updatedAt = repo.getUpdatedAt() != null ? repo.getUpdatedAt() : repo.getPushedAt();

        long daysSinceCreation = Duration.between(createdAt, Instant.now()).toDays();
        if (daysSinceCreation == 0) daysSinceCreation = 1;

        double growthVelocity = (double) stars / daysSinceCreation;

        long daysSinceUpdate = Duration.between(updatedAt, Instant.now()).toDays();
        int recencyWeight = daysSinceUpdate <= 7 ? 150 : daysSinceUpdate <= 30 ? 80 : daysSinceUpdate <= 90 ? 30 : 5;

        double forkRatioScore = (forks == 0) ? 0 : Math.min((double) forks / stars, 1.0) * 50;

        int sizePenalty = Math.min(repo.getSize() / 500, 40); // reduce score for huge repos
        
        return (int) (
                (stars * 1.5)
                        + (forks * 1.2)
                        + (growthVelocity * 5)
                        + recencyWeight
                        + forkRatioScore
                        - sizePenalty
        );
    }
}
