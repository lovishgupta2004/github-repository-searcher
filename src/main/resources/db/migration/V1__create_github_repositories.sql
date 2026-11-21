CREATE TABLE github_repositories (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_name VARCHAR(255) NOT NULL,
    description VARCHAR(512),
    language VARCHAR(255),
    stars BIGINT NOT NULL,
    forks BIGINT NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);
