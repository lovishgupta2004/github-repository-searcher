package com.example.github_repository_searcher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubSearchRequest {
    @NotBlank
    private String query;
    private String language;
    @Pattern(regexp = "stars|forks|updated")
    private String sort;
}
