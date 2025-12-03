package com.scoring.github.popularity_service.unit;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;
import com.scoring.github.popularity_service.scoringstrategy.SimpleScoringStrategy;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleScoringStrategyTest {

    private final SimpleScoringStrategy strategy = new SimpleScoringStrategy();

    @Test
    void shouldCalculateScore_ForRecentRepo() {
        GithubSearchResponseDto.Item repo = new GithubSearchResponseDto.Item();
        repo.setStargazersCount(50);
        repo.setForks(10);
        repo.setUpdatedAt(Instant.now().minusSeconds(5 * 24 * 3600)); // 5 days ago

        int score = strategy.calculateScore(repo);

        // expected = stars * 2 + forks + recencyWeight
        assertThat(score).isGreaterThan(160);
    }

    @Test
    void shouldCalculateScore_ForOldRepo() {
        GithubSearchResponseDto.Item repo = new GithubSearchResponseDto.Item();
        repo.setStargazersCount(10);
        repo.setForks(5);
        repo.setUpdatedAt(Instant.now().minusSeconds(200 * 24 * 3600)); // 200 days

        int score = strategy.calculateScore(repo);

        assertThat(score).isEqualTo(10 * 2 + 5 + 10);
    }
}
