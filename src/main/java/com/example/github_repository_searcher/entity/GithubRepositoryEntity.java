package com.example.github_repository_searcher.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity 
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter 
public class GithubRepositoryEntity {
    @Id
    private Long id;
    private String name;
    private String ownerName;
    private String description;
    private String language;
    private Long stars;
    private Long forks;
    private Instant lastUpdated;
}
