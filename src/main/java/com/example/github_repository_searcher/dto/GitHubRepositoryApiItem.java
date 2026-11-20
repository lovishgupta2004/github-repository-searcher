package com.example.github_repository_searcher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubRepositoryApiItem {
    private Long id;
    private String name;
    private String description;
    private Owner owner;
    private String language;

    @JsonProperty("stargazers_count")
    private Long stargazersCount;

    @JsonProperty("forks_count")
    private Long forksCount;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Owner {
        private String login;
    }
}
