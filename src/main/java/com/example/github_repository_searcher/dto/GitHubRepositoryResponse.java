package com.example.github_repository_searcher.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class GitHubRepositoryResponse {
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String language;
    private Long stars;
    private Long forks;
    private Instant lastUpdated;
}
