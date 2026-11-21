# GitHub Repository Searcher

A Java 21 and Spring Boot REST API that searches GitHub repositories, saves the results to PostgreSQL, and lets you filter and sort the saved repositories. Neon can host the PostgreSQL database. Swagger UI provides interactive API documentation; you can also use Postman, `curl`, or another HTTP client.

## Features

- Search GitHub repositories by name, with optional programming-language and sort filters.
- Save the results in PostgreSQL. GitHub's repository ID is the primary key, so a later search updates a repository already stored.
- List saved repositories, optionally filtered by language and minimum stars.
- Sort saved repositories by stars, forks, or last-updated time.
- Explore and try the API from Swagger UI with automatically generated OpenAPI documentation.
- Apply the database schema automatically at application startup with Flyway.
- Validate request fields and validate the database schema against the JPA entity.

GitHub API search uses its default page size (currently 30 results). Pagination and GitHub authentication are not implemented, so anonymous GitHub API rate limits apply.

## Requirements

- Java 21 for running the project locally.
- Docker for the container instructions below.
- A PostgreSQL database, such as a Neon project.
- Internet access from the application to GitHub's API and the PostgreSQL host.

Docker packages and runs the application; Neon hosts the database. You can run the application directly with Maven instead of Docker.

## Deploy publicly with Render

The project includes a Render Blueprint in `render.yaml` and is configured to listen on Render's `PORT` environment variable. Render builds and runs the existing Dockerfile; Neon remains the database.

1. Push the project, including `render.yaml`, to a GitHub repository accessible to your Render account.
2. In Render, choose **New** → **Blueprint**, connect that repository, and apply the `render.yaml` configuration.
3. In the Render service's environment settings, provide these values:
   - `DATABASE_URL`: `jdbc:postgresql://<neon-host>/neondb?sslmode=require&channelBinding=require`
   - `DATABASE_USERNAME`: your Neon database role
   - `DATABASE_PASSWORD`: the current password for that role
4. Save the settings and wait for the deployment and health check to succeed.
5. Render assigns a public service URL, such as `https://<service-name>.onrender.com`. Open `<service-url>/swagger-ui.html` for the interactive API docs or `<service-url>/api/github/repositories` to list stored repositories.

Do not commit database credentials to `render.yaml` or GitHub. Enter them only in Render's environment settings. Do not copy a Neon URL containing `user` or `password` query parameters; keep credentials in their separate environment variables. Render injects `PORT` automatically. The Blueprint uses Render's free service plan, which can sleep when idle and take longer to respond to the first request after inactivity.

## Configure the database

In Neon, open your project's connection details and copy the PostgreSQL host and database role. Create a local `.env` file in the project root:

```env
DATABASE_URL=jdbc:postgresql://<neon-host>/neondb?sslmode=require&channelBinding=require
DATABASE_USERNAME=<neon-database-role>
DATABASE_PASSWORD=<neon-database-password>
```

Replace the placeholders with your Neon connection details. Keep the username and password in their separate variables; do not put them in the URL. The URL must use the `jdbc:postgresql://` format.

Do not commit `.env` or paste database credentials into issues or chat. `.env` is excluded by the repository's ignore rules.

Flyway applies `src/main/resources/db/migration/V1__create_github_repositories.sql` during startup. No manual table creation is required. Hibernate is configured to validate the migrated schema rather than create tables.

## Run with Docker

Run these commands from the project root:

```powershell
docker build -t github-repository-searcher:dev .
docker run -d --name github-repository-searcher --env-file .env -p 8080:8080 github-repository-searcher:dev
```

The API is available at `http://localhost:8080`.

Open the interactive API documentation at [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html). The generated OpenAPI JSON is available at `http://localhost:8080/v3/api-docs`.

View application logs:

```powershell
docker logs -f github-repository-searcher
```

Stop and remove the container:

```powershell
docker stop github-repository-searcher
docker rm github-repository-searcher
```

If Docker logs show `UnknownHostException` for the Neon host, the DNS resolver available to Docker may be failing. As a diagnostic, try starting the container with a public DNS resolver:

```powershell
docker run -d --name github-repository-searcher --dns 1.1.1.1 --env-file .env -p 8080:8080 github-repository-searcher:dev
```

Use the DNS option only if needed for your network. If port 8080 is already in use, publish another host port, for example `-p 8081:8080`, and use `http://localhost:8081`.

## Run locally with Maven

Spring Boot does not load `.env` automatically when started with Maven. Set the three environment variables in PowerShell first, then run the application:

```powershell
$env:DATABASE_URL = "jdbc:postgresql://<neon-host>/neondb?sslmode=require&channelBinding=require"
$env:DATABASE_USERNAME = "<neon-database-role>"
$env:DATABASE_PASSWORD = "<neon-database-password>"
.\mvnw.cmd spring-boot:run
```

To run the tests:

```powershell
.\mvnw.cmd test
```

The Dockerfile skips tests while building the image. Run the test command separately when you want to test the project.

## API

Base URL:

```text
http://localhost:8080
```

You can also browse the available endpoints and send requests from [Swagger UI](http://localhost:8080/swagger-ui.html) while the application is running.

### Search GitHub and save results

`POST /api/github/search`

Request body:

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

Fields:

| Field | Required | Description |
| --- | --- | --- |
| `query` | Yes | Repository-name search text; must not be blank. |
| `language` | No | Optional GitHub language filter, such as `Java`. |
| `sort` | No | GitHub sort order: `stars`, `forks`, or `updated`. |

The search is restricted to repository names. Matching results are saved to PostgreSQL and returned in the response:

```json
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [
    {
      "id": 123456,
      "name": "example",
      "description": "Example repository",
      "owner": "example-owner",
      "language": "Java",
      "stars": 1200,
      "forks": 250,
      "lastUpdated": "2025-01-15T10:30:00Z"
    }
  ]
}
```

Example using `curl.exe` from PowerShell:

```powershell
curl.exe -X POST "http://localhost:8080/api/github/search" `
  -H "Content-Type: application/json" `
  -d '{"query":"spring boot","language":"Java","sort":"stars"}'
```

### List saved repositories

`GET /api/github/repositories`

All filters are optional:

| Parameter | Description |
| --- | --- |
| `language` | Filter by language; comparison is case-insensitive. |
| `minStars` | Include repositories with at least this many stars; must be zero or greater. |
| `sort` | Sort by `stars`, `forks`, or `updated`; defaults to `stars`. Results are descending. |

Examples:

```text
GET http://localhost:8080/api/github/repositories
GET http://localhost:8080/api/github/repositories?language=Java
GET http://localhost:8080/api/github/repositories?language=Java&minStars=1000&sort=stars
GET http://localhost:8080/api/github/repositories?sort=updated
```

The response contains a `repositories` array using the same repository fields shown above:

```json
{
  "repositories": []
}
```

Invalid search queries, negative `minStars`, or unsupported sort values fail request validation. There is no custom error response format.

## Check the database in Neon

After the application starts, Flyway records its migration in `flyway_schema_history`. Run these queries in Neon SQL Editor to check the migration and saved rows:

```sql
SELECT version, description, success
FROM flyway_schema_history;
```

```sql
SELECT id, name, owner_name, language, stars, forks, last_updated
FROM github_repositories
ORDER BY stars DESC
LIMIT 10;
```

To count the stored repositories:

```sql
SELECT COUNT(*) AS repository_count
FROM github_repositories;
```

Run the `POST /api/github/search` request before checking repository rows; the table can be empty until a search is performed.

## Troubleshooting

- **`UnknownHostException` in Docker logs:** Docker cannot resolve the Neon hostname. Verify the host in Neon connection details and try the optional `--dns 1.1.1.1` run command above.
- **Database authentication failure:** Check that `DATABASE_USERNAME` is the Neon database role and `DATABASE_PASSWORD` is its current password.
- **Missing `github_repositories` table:** Confirm the Flyway migration file is packaged and the Flyway startup log reports the migration. Do not manually create a different schema while using Flyway.
- **Neon SQL Editor says `Failed to fetch`:** This can be a browser/dashboard connectivity issue. Refresh or sign in again, try a private browser window, and check VPN or browser extensions. If the application's `GET /api/github/repositories` endpoint returns HTTP 200, the application can reach and query the database.
- **Port 8080 is busy:** Publish a different host port, such as `-p 8081:8080`, and update the API base URL accordingly.
