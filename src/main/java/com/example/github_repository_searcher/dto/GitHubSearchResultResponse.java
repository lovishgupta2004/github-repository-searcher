package com.example.github_repository_searcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GitHubSearchResultResponse {
    private final String message;
    private final List<GitHubRepositoryResponse> repositories;
}
