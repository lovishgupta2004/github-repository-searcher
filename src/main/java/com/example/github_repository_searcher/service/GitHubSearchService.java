package com.example.github_repository_searcher.service;

import com.example.github_repository_searcher.client.GitHubApiClient;
import com.example.github_repository_searcher.dto.GitHubRepositoryApiItem;
import com.example.github_repository_searcher.dto.GitHubRepositoriesResponse;
import com.example.github_repository_searcher.dto.GitHubRepositoryFilterRequest;
import com.example.github_repository_searcher.dto.GitHubRepositoryResponse;
import com.example.github_repository_searcher.dto.GitHubSearchApiResponse;
import com.example.github_repository_searcher.dto.GitHubSearchRequest;
import com.example.github_repository_searcher.dto.GitHubSearchResultResponse;
import com.example.github_repository_searcher.entity.GithubRepositoryEntity;
import com.example.github_repository_searcher.repository.GithubRepositoryRepository;
import org.springframework.data.domain.Sort;
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

    public GitHubSearchResultResponse searchRepositories(GitHubSearchRequest request) {
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
        List<GithubRepositoryEntity> savedEntities = githubRepositoryRepository.saveAll(entities);
        List<GitHubRepositoryResponse> repositories = savedEntities.stream()
                .map(this::toResponse)
                .toList();

        return new GitHubSearchResultResponse(
                "Repositories fetched and saved successfully",
                repositories);
    }

    public GitHubRepositoriesResponse getRepositories(GitHubRepositoryFilterRequest request) {
        String language = request.getLanguage();
        if (language != null && language.isBlank()) {
            language = null;
        }

        String sort = request.getSort();
        if (sort == null || sort.isBlank()) {
            sort = "stars";
        }

        Sort databaseSort = switch (sort) {
            case "stars" -> Sort.by(Sort.Direction.DESC, "stars");
            case "forks" -> Sort.by(Sort.Direction.DESC, "forks");
            case "updated" -> Sort.by(Sort.Direction.DESC, "lastUpdated");
            default -> throw new IllegalArgumentException("Sort must be stars, forks, or updated");
        };

        List<GitHubRepositoryResponse> repositories = githubRepositoryRepository.findByFilters(
                        language,
                        request.getMinStars(),
                        databaseSort)
                .stream()
                .map(this::toResponse)
                .toList();

        return new GitHubRepositoriesResponse(repositories);
    }

    private GithubRepositoryEntity toEntity(GitHubRepositoryApiItem item) {
        if (item.getId() == null
                || item.getName() == null
                || item.getOwner() == null
                || item.getOwner().getLogin() == null
                || item.getStargazersCount() == null
                || item.getForksCount() == null
                || item.getUpdatedAt() == null) {
            throw new IllegalStateException("GitHub repository response is missing required fields");
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

    private GitHubRepositoryResponse toResponse(GithubRepositoryEntity entity) {
        GitHubRepositoryResponse response = new GitHubRepositoryResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setOwner(entity.getOwnerName());
        response.setLanguage(entity.getLanguage());
        response.setStars(entity.getStars());
        response.setForks(entity.getForks());
        response.setLastUpdated(entity.getLastUpdated());
        return response;
    }
}
