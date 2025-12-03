package com.scoring.github.popularity_service.unit;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;
import com.scoring.github.popularity_service.dto.PopularityResponseDto;
import com.scoring.github.popularity_service.remote.GithubApi;
import com.scoring.github.popularity_service.scoringstrategy.AdvancedScoringStrategy;
import com.scoring.github.popularity_service.scoringstrategy.SimpleScoringStrategy;
import com.scoring.github.popularity_service.service.RepositoryScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class RepositoryScoringServiceTest {

    private final GithubApi githubApi = Mockito.mock(GithubApi.class);
    private RepositoryScoringService service;

    @BeforeEach
    void setup() {
        service = new RepositoryScoringService(
                githubApi,
                List.of(new SimpleScoringStrategy(), new AdvancedScoringStrategy())
        );
    }

    private GithubSearchResponseDto mockGithubResponse(int stars, int forks, Instant updatedAt) {
        GithubSearchResponseDto response = new GithubSearchResponseDto();
        GithubSearchResponseDto.Item item = new GithubSearchResponseDto.Item();
        item.setName("TestRepo");
        item.setStargazersCount(stars);
        item.setForks(forks);
        item.setCreatedAt(Instant.now().minusSeconds(10000));
        item.setUpdatedAt(updatedAt);
        item.setPushedAt(updatedAt);
        item.setLanguage("java");
        item.setSize(1000);

        response.setItems(List.of(item));
        return response;
    }

    @Test
    void shouldReturnPopularRepositoriesSortedByScore() {

        // Repo A: more stars → expected to be higher in final sorted list
        GithubSearchResponseDto.Item repoA = new GithubSearchResponseDto.Item();
        repoA.setName("RepoA");
        repoA.setStargazersCount(100);
        repoA.setForks(10);
        repoA.setCreatedAt(Instant.now().minusSeconds(100000));
        repoA.setUpdatedAt(Instant.now().minusSeconds(1));
        repoA.setPushedAt(Instant.now().minusSeconds(1));
        repoA.setSize(500);

        // Repo B: fewer stars → lower score
        GithubSearchResponseDto.Item repoB = new GithubSearchResponseDto.Item();
        repoB.setName("RepoB");
        repoB.setStargazersCount(20);
        repoB.setForks(5);
        repoB.setCreatedAt(Instant.now().minusSeconds(200000));
        repoB.setUpdatedAt(Instant.now().minusSeconds(5 * 24 * 3600));
        repoB.setPushedAt(Instant.now().minusSeconds(5 * 24 * 3600));
        repoB.setSize(300);

        GithubSearchResponseDto firstPage = new GithubSearchResponseDto();
        firstPage.setItems(List.of(repoA, repoB));

        GithubSearchResponseDto emptyPage = new GithubSearchResponseDto();
        emptyPage.setItems(List.of());

        when(githubApi.searchRepositories(anyString(), anyInt(), eq(1)))
                .thenReturn(firstPage);

        when(githubApi.searchRepositories(anyString(), anyInt(), gt(1)))
                .thenReturn(emptyPage);

        List<PopularityResponseDto> result = service.getRepositoriesByPopularity(
                "java",
                null,
                10,
                "simple"
        );

        assertThat(result).hasSize(2);

        assertThat(result.get(0).getRepositoryName()).isEqualTo("RepoA");
        assertThat(result.get(1).getRepositoryName()).isEqualTo("RepoB");
    }


    @Test
    void shouldUseAdvancedAlgorithmWhenSelected() {
        GithubSearchResponseDto firstPage = mockGithubResponse(200, 50, Instant.now().minusSeconds(5));

        GithubSearchResponseDto emptyPage = new GithubSearchResponseDto();
        emptyPage.setItems(List.of());

        when(githubApi.searchRepositories(anyString(), anyInt(), eq(1)))
                .thenReturn(firstPage);

        when(githubApi.searchRepositories(anyString(), anyInt(), eq(2)))
                .thenReturn(emptyPage);

        List<PopularityResponseDto> result = service.getRepositoriesByPopularity(
                "java",
                null,
                10,
                "advanced"
        );

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getPopularityScore()).isGreaterThan(200);
    }


    @Test
    void shouldStopWhenGithubReturnsEmptyList() {
        GithubSearchResponseDto empty = new GithubSearchResponseDto();
        empty.setItems(List.of());

        when(githubApi.searchRepositories(anyString(), anyInt(), anyInt()))
                .thenReturn(empty);

        List<PopularityResponseDto> result = service.getRepositoriesByPopularity(
                "java",
                null,
                10,
                "simple"
        );

        assertThat(result).isEmpty();
    }
}
