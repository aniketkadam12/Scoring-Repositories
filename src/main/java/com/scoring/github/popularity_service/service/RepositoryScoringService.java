package com.scoring.github.popularity_service.service;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;
import com.scoring.github.popularity_service.dto.PopularityResponseDto;
import com.scoring.github.popularity_service.remote.GithubApi;
import com.scoring.github.popularity_service.scoringstrategy.ScoringStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepositoryScoringService {

    private static final int GITHUB_PAGE_SIZE = 100;
    private static final int GITHUB_MAX_RESULTS = 1000;

    private final GithubApi githubApi;
    private final Map<String, ScoringStrategy> scoringStrategies;

    public RepositoryScoringService(GithubApi githubApi,
                                    List<ScoringStrategy> strategies) {
        this.githubApi = githubApi;
        this.scoringStrategies = strategies.stream()
                .collect(Collectors.toMap(ScoringStrategy::getName, s -> s));
    }

    public List<String> getSupportedAlgorithms() {
        return new ArrayList<>(scoringStrategies.keySet());
    }

    public List<PopularityResponseDto> getRepositoriesByPopularity(
            String language,
            LocalDate createdAfterDate,
            int limit,
            String algorithmName
    ) {
        ScoringStrategy strategy = scoringStrategies.get(algorithmName);

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unsupported algorithm: " + algorithmName +
                            ". Supported: " + scoringStrategies.keySet()
            );
        }

        String query = buildGithubQuery(language, createdAfterDate);

        List<PopularityResponseDto> result = new ArrayList<>();
        int page = 1;

        while (result.size() < limit && (page - 1) * GITHUB_PAGE_SIZE < GITHUB_MAX_RESULTS) {

            GithubSearchResponseDto response =
                    githubApi.searchRepositories(query, GITHUB_PAGE_SIZE, page);

            if (response.getItems() == null || response.getItems().isEmpty())
                break;

            for (GithubSearchResponseDto.Item repo : response.getItems()) {
                if (result.size() >= limit)
                    break;

                int score = strategy.calculateScore(repo);
                result.add(mapToPopularityResponse(repo, score));
            }
            page++;
        }

        result.sort(Comparator.comparingInt(PopularityResponseDto::getPopularityScore).reversed());
        return result;
    }

    private String buildGithubQuery(String language, LocalDate createdAfterDate) {
        StringBuilder q = new StringBuilder();

        if (language != null && !language.isBlank()) {
            q.append("language:").append(language.trim());
        }

        if (createdAfterDate != null) {
            if (!q.isEmpty()) q.append("+");
            q.append("created:>=").append(createdAfterDate);
        }

        if (q.isEmpty()) {
            q.append("stars:>0");
        }

        return q.toString();
    }

    private PopularityResponseDto mapToPopularityResponse(GithubSearchResponseDto.Item repo, int score) {
        PopularityResponseDto dto = new PopularityResponseDto();
        dto.setRepositoryName(repo.getName());
        dto.setRepositoryUrl(repo.getHtmlUrl());
        dto.setCreatedAt(repo.getCreatedAt());
        dto.setLastUpdatedAt(repo.getUpdatedAt() != null ? repo.getUpdatedAt() : repo.getPushedAt());
        dto.setPopularityScore(score);
        dto.setStars(repo.getStargazersCount());
        dto.setForks(repo.getForks());
        return dto;
    }
}
