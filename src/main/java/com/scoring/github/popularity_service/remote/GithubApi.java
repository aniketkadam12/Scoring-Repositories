package com.scoring.github.popularity_service.remote;

import com.scoring.github.popularity_service.dto.GithubSearchResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GithubApi {

    private final WebClient webClient;

    public GithubApi(WebClient githubWebClient) {
        this.webClient = githubWebClient;
    }

    public GithubSearchResponseDto searchRepositories(String rawQuery, int perPage, int page) {

        String url =
                "/search/repositories"
                        + "?q=" + rawQuery     // RAW — to avoid re-encoding problems
                        + "&per_page=" + perPage
                        + "&page=" + page;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GithubSearchResponseDto.class)
                .block();
    }
}
