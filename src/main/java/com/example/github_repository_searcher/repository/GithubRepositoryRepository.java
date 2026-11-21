package com.example.github_repository_searcher.repository;

import com.example.github_repository_searcher.entity.GithubRepositoryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GithubRepositoryRepository extends JpaRepository<GithubRepositoryEntity, Long> {
    @Query("""
            SELECT repository FROM GithubRepositoryEntity repository
            WHERE (:language IS NULL OR LOWER(repository.language) = LOWER(:language))
              AND (:minStars IS NULL OR repository.stars >= :minStars)
            """)
    List<GithubRepositoryEntity> findByFilters(
            @Param("language") String language,
            @Param("minStars") Long minStars,
            Sort sort);
}
