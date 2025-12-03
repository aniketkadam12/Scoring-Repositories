package com.scoring.github.popularity_service.unit;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;
import com.scoring.github.popularity_service.scoringstrategy.AdvancedScoringStrategy;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AdvancedScoringStrategyTest {

    private final AdvancedScoringStrategy strategy = new AdvancedScoringStrategy();

    @Test
    void shouldGiveHighScore_ForFastGrowingAndRecentlyUpdatedRepo() {
        GithubSearchResponseDto.Item repo = new GithubSearchResponseDto.Item();
        repo.setStargazersCount(300);
        repo.setForks(100);
        repo.setCreatedAt(Instant.now().minusSeconds(30 * 24 * 3600));
        repo.setUpdatedAt(Instant.now().minusSeconds(2 * 24 * 3600));

        int score = strategy.calculateScore(repo);

        assertThat(score).isGreaterThan(500);
    }

    @Test
    void shouldGiveLowerScore_ForOldInactiveRepo() {
        GithubSearchResponseDto.Item repo = new GithubSearchResponseDto.Item();
        repo.setStargazersCount(10);
        repo.setForks(2);
        repo.setCreatedAt(Instant.now().minusSeconds(500 * 24 * 3600));
        repo.setUpdatedAt(Instant.now().minusSeconds(400 * 24 * 3600));

        int score = strategy.calculateScore(repo);

        assertThat(score).isLessThan(100);
    }
}

