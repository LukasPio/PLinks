# 🔗 PLinks — URL Shortener API

A URL shortener built with **Java 21 + Spring Boot 3**, featuring custom slugs, expiration time, click tracking, and automatic QR Code generation.

---

## Features

- Shorten any valid `https://` URL with a random or custom slug
- Optional link expiration (TTL in seconds)
- Click counter per shortened link
- QR Code generation (PNG, returned as `byte[]`)
- Global exception handling with structured JSON error responses
- Database versioning with Flyway migrations
- PostgreSQL persistence

---

## Endpoints

| Method | Path        | Description                              |
|--------|-------------|------------------------------------------|
| POST   | `/short`    | Shorten a URL                            |
| GET    | `/{slug}`   | Redirect to the original URL             |
| POST   | `/clicks`   | Get the click count for a shortened link |

---

## Request & Response Format

All responses follow a standard envelope:

```json
{
  "message": "Link shortened successfully",
  "timestamp": "2024-01-01T00:00:00.000Z",
  "statusCode": 200,
  "data": { ... }
}
```

### POST `/short`

**Request body:**
```json
{
  "url": "https://example.com/some/long/path",
  "slug": "my-link",
  "expiresAfter": 3600,
  "generateQrCode": true
}
```

> All fields except `url` are optional. If `slug` is omitted, a random 8-character slug is generated.

**Response:**
```json
{
  "message": "Link shortened successfully",
  "timestamp": "...",
  "statusCode": 200,
  "data": {
    "shortenedUrl": "http://localhost:8080/my-link",
    "qrCode": "<bytes>"
  }
}
```

### POST `/clicks`

**Request body:**
```json
{
  "slug": "my-link"
}
```

---

## Error Responses

| Situation                | Status |
|--------------------------|--------|
| Invalid or non-https URL | `400`  |
| Slug already registered  | `400`  |
| Slug not found           | `404`  |
| Link expired             | `410`  |

---

## Getting Started

### Prerequisites

- Docker + Docker Compose

### Run

```bash
docker-compose up
```

The API will be available at `http://localhost:8080`.

### Configuration

The database connection is configured in `application.yml`. When using Docker Compose, it is set up automatically. To run locally without Docker, update the datasource settings:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/Plinks
    username: postgres
    password: secret123
```

---

## Project Structure

```
src/
├── main/
│   ├── java/com/lucas/plinks/
│   │   ├── PLinksApplication.java
│   │   ├── Link.java                   # JPA entity
│   │   ├── LinkController.java         # REST endpoints
│   │   ├── LinkService.java            # Business logic + QR Code generation
│   │   ├── LinkRepository.java         # Spring Data JPA
│   │   ├── ApiResponse.java            # Standard response envelope
│   │   ├── Constants.java
│   │   ├── *DTO.java                   # Request / Response records
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       └── *.java                  # Custom exceptions
│   └── resources/
│       ├── application.yml
│       └── db/migration/               # Flyway versioned migrations
```

---

## Tech Stack

| Layer       | Technology                  |
|-------------|-----------------------------|
| Language    | Java 21                     |
| Framework   | Spring Boot 3               |
| Database    | PostgreSQL                  |
| Migrations  | Flyway                      |
| ORM         | Spring Data JPA / Hibernate |
| QR Code     | nayuki/QR-Code-generator    |
| Boilerplate | Lombok                      |
| Infra       | Docker + Docker Compose     |

---

## Roadmap

- [ ] Authentication — protect endpoints with JWT
- [ ] Custom domains — allow using a custom base URL per user
- [ ] Dashboard — UI to manage and visualize links and click stats
- [ ] Rate limiting — prevent abuse on the `/short` endpoint
- [ ] Analytics — detailed click history with timestamps and geolocation

---

## License

MIT
