package com.example.github_repository_searcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GitHubRepositoriesResponse {
    private final List<GitHubRepositoryResponse> repositories;
}
