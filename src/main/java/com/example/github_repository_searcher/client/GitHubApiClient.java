package com.example.github_repository_searcher.client;

import com.example.github_repository_searcher.dto.GitHubSearchApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubApiClient {
    private final RestClient restClient;

    public GitHubApiClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader(HttpHeaders.USER_AGENT, "github-repository-searcher")
                .build();
    }

    public GitHubSearchApiResponse searchRepositories(String query, String language, String sort) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query must not be blank");
        }

        StringBuilder githubQuery = new StringBuilder(query.trim()).append(" in:name");
        if (language != null && !language.isBlank()) {
            githubQuery.append(" language:").append(language.trim());
        }

        GitHubSearchApiResponse response = restClient.get()
                .uri(uriBuilder -> {
                    var requestUri = uriBuilder.path("/search/repositories")
                            .queryParam("q", githubQuery.toString());
                    if (sort != null && !sort.isBlank()) {
                        requestUri.queryParam("sort", sort);
                    }
                    return requestUri.build();
                })
                .retrieve()
                .body(GitHubSearchApiResponse.class);

        if (response == null) {
            throw new IllegalStateException("GitHub API returned an empty search response");
        }

        return response;
    }
}
