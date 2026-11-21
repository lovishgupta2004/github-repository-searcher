package com.example.github_repository_searcher.controller;

import com.example.github_repository_searcher.dto.GitHubSearchRequest;
import com.example.github_repository_searcher.dto.GitHubSearchResultResponse;
import com.example.github_repository_searcher.dto.GitHubRepositoriesResponse;
import com.example.github_repository_searcher.dto.GitHubRepositoryFilterRequest;
import com.example.github_repository_searcher.service.GitHubSearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
public class GitHubSearchController {
    private final GitHubSearchService gitHubSearchService;

    public GitHubSearchController(GitHubSearchService gitHubSearchService) {
        this.gitHubSearchService = gitHubSearchService;
    }

    @PostMapping("/search")
    public ResponseEntity<GitHubSearchResultResponse> searchRepositories(
            @Valid @RequestBody GitHubSearchRequest request) {
        return ResponseEntity.ok(gitHubSearchService.searchRepositories(request));
    }

    @GetMapping("/repositories")
    public ResponseEntity<GitHubRepositoriesResponse> getRepositories(
            @Valid @ModelAttribute GitHubRepositoryFilterRequest request) {
        return ResponseEntity.ok(gitHubSearchService.getRepositories(request));
    }
}
