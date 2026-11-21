package com.example.github_repository_searcher.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubRepositoryFilterRequest {
    private String language;

    @PositiveOrZero
    private Long minStars;

    @Pattern(regexp = "stars|forks|updated")
    private String sort;
}
