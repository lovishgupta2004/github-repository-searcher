package com.example.github_repository_searcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "GitHub Repository Searcher API",
		version = "1.0",
		description = "Search GitHub repositories and retrieve saved results."
))
public class GithubRepositorySearcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubRepositorySearcherApplication.class, args);
	}

}
