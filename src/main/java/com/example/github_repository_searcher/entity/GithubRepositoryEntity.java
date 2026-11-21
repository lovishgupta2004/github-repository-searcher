package com.example.github_repository_searcher.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "github_repositories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GithubRepositoryEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(length = 512)
    private String description;

    private String language;

    @Column(nullable = false)
    private Long stars;

    @Column(nullable = false)
    private Long forks;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;
}
