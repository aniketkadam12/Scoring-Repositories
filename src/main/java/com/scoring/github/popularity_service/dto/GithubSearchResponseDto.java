package com.scoring.github.popularity_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
public class GithubSearchResponseDto {

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("incomplete_results")
    private boolean incompleteResults;

    private List<Item> items;

    @Setter
    @Getter
    public static class Item {

        private String name;

        private String description;

        @JsonProperty("html_url")
        private String htmlUrl;

        @JsonProperty("created_at")
        private Instant createdAt;

        @JsonProperty("updated_at")
        private Instant updatedAt;

        @JsonProperty("pushed_at")
        private Instant pushedAt;

        @JsonProperty("stargazers_count")
        private int stargazersCount;

        private String language;

        private int forks;

        private int size;
    }
}
