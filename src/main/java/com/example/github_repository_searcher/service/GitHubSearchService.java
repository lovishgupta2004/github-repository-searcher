package com.example.github_repository_searcher.service;

import com.example.github_repository_searcher.client.GitHubApiClient;
import com.example.github_repository_searcher.dto.GitHubRepositoryApiItem;
import com.example.github_repository_searcher.dto.GitHubSearchApiResponse;
import com.example.github_repository_searcher.dto.GitHubSearchRequest;
import com.example.github_repository_searcher.entity.GithubRepositoryEntity;
import com.example.github_repository_searcher.repository.GithubRepositoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubSearchService {
    private final GitHubApiClient gitHubApiClient;
    private final GithubRepositoryRepository githubRepositoryRepository;

    public GitHubSearchService(
            GitHubApiClient gitHubApiClient,
            GithubRepositoryRepository githubRepositoryRepository) {
        this.gitHubApiClient = gitHubApiClient;
        this.githubRepositoryRepository = githubRepositoryRepository;
    }

    public List<GithubRepositoryEntity> searchRepositories(GitHubSearchRequest request) {
        GitHubSearchApiResponse response = gitHubApiClient.searchRepositories(
                request.getQuery(),
                request.getLanguage(),
                request.getSort());

        if (response.getItems() == null) {
            throw new IllegalStateException("GitHub search response is missing its items");
        }

        List<GithubRepositoryEntity> entities = response.getItems().stream()
                .map(this::toEntity)
                .toList();
        return githubRepositoryRepository.saveAll(entities);
    }

    private GithubRepositoryEntity toEntity(GitHubRepositoryApiItem item) {
        if (item.getId() == null || item.getOwner() == null || item.getOwner().getLogin() == null) {
            throw new IllegalStateException("GitHub repository response is missing its ID or owner");
        }

        GithubRepositoryEntity entity = new GithubRepositoryEntity();
        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setOwnerName(item.getOwner().getLogin());
        entity.setDescription(item.getDescription());
        entity.setLanguage(item.getLanguage());
        entity.setStars(item.getStargazersCount());
        entity.setForks(item.getForksCount());
        entity.setLastUpdated(item.getUpdatedAt());
        return entity;
    }
}
