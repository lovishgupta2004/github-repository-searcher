package com.example.github_repository_searcher.repository;
import com.example.github_repository_searcher.entity.GithubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepositoryRepository extends JpaRepository<GithubRepositoryEntity, Long> {

}
