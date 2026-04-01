# LpTasks API

REST API for task management with JWT authentication, built with **Kotlin + Spring Boot**.

---

## 🛠️ Tech Stack

- **Kotlin** + **Spring Boot 3.3**
- **PostgreSQL** — data persistence
- **Redis** — caching
- **Spring Security** + **JWT (Auth0)** — authentication and authorization
- **Docker Compose** — local infrastructure
- **Maven** — dependency management

---

## 📋 Prerequisites

- Java 21+
- Maven
- Docker and Docker Compose

---

## 🚀 Getting Started

**1. Start the infrastructure (database and cache):**

```bash
docker-compose up -d
```

**2. Run the application:**

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## 🔐 Authentication

The API uses **JWT Bearer Token**. To access protected endpoints, include the header:

```
Authorization: Bearer <your_token>
```

### Auth endpoints

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `POST` | `/app/auth/register` | Public | Register a new user |
| `POST` | `/app/auth/login` | Public | Login and get a token |

#### Register
```json
POST /app/auth/register
{
  "email": "user@email.com",
  "password": "password123",
  "isAdmin": false
}
```

#### Login
```json
POST /app/auth/login
{
  "email": "user@email.com",
  "password": "password123"
}
```
**Response:**
```json
{
  "body": { "token": "eyJhbGci..." },
  "message": "Successfully loged in",
  "statusCode": 200
}
```

---

## ✅ Task Endpoints

All endpoints below require authentication.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/app/tasks` | List all tasks |
| `GET` | `/app/tasks/id?id={id}` | Get task by ID |
| `GET` | `/app/tasks/title?taskTitle={title}` | Get tasks by title |
| `GET` | `/app/tasks/category?category={category}` | Filter by category |
| `GET` | `/app/tasks/sortByPriority?sortOrder={asc\|desc}` | Sort by priority |
| `POST` | `/app/tasks` | Create one or more tasks |
| `PUT` | `/app/tasks/{id}` | Update a task |
| `DELETE` | `/app/tasks/{id}` | Delete a task |

### Request body (create/update)

```json
[
  {
    "title": "Study Kotlin",
    "description": "Review coroutines and flows",
    "category": "STUDY",
    "priority": "HIGH"
  }
]
```

### Available categories

| Value |
|-------|
| `WORK` |
| `STUDY` |
| `HOBBY` |
| `OTHER` |

### Available priorities

| Value |
|-------|
| `LOW` |
| `MEDIUM` |
| `HIGH` |

---

## 📦 Project Structure

```
src/main/kotlin/com/lucas/lptasks/
├── controller/       # REST layer
├── service/          # Business logic
├── repository/       # Database access
├── model/            # JPA entities
├── dto/              # Data transfer objects
├── security/         # JWT filters and Spring Security config
├── exception/        # Custom exceptions and global handler
├── enum/             # Category and priority enums
└── utils/            # Helpers (validation, ApiResponse)
```

---

## 🗄️ Configuration

Settings are defined in `src/main/resources/application.yml`. Default values:

| Property | Default |
|----------|---------|
| `server.port` | `8080` |
| `datasource.url` | `jdbc:postgresql://127.0.0.1:5432/LpTasks` |
| `datasource.username` | `lukas` |
| `datasource.password` | `mistery123` |
| `cache.type` | `redis` |
| `token.secret` | `encrypted123` |

> ⚠️ In production, replace `token.secret` with a strong value and externalize credentials via environment variables.

---

## 🐳 Docker Compose

The `docker-compose.yml` file starts two services:

- **PostgreSQL 13** on port `5432` — auto-initialized with `initialize.sql`
- **Redis 7.4** on port `6379`

```bash
# Start
docker-compose up -d

# Stop
docker-compose down
```

---

## 🗺️ Roadmap

- [ ] **Unit and integration tests** — cover services, controllers and security filters with JUnit 5 and MockK
- [ ] **Cache annotations** — apply `@Cacheable` and `@CacheEvict` on read endpoints (currently Redis is configured but unused)
- [ ] **Task status field** — add `status` with values like `TODO`, `IN_PROGRESS`, `DONE`
- [ ] **Swagger / OpenAPI** — interactive API docs via SpringDoc
- [ ] **Flyway migrations** — replace `ddl-auto: update` with versioned schema management

---

## 📐 Response format

All endpoints return the same envelope:

```json
{
  "body": {},
  "message": "Descriptive message",
  "statusCode": 200
}
```
