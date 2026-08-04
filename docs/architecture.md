# System Architecture

```text
                   ┌─────────────────────┐
                   │      Client         │
                   │ Browser / Postman   │
                   └──────────┬──────────┘
                              │ HTTP/HTTPS
                              ▼
                   ┌─────────────────────┐
                   │   Spring Boot API   │
                   │    REST Controllers │
                   └──────────┬──────────┘
                              │
          ┌───────────────────┼────────────────────┐
          ▼                   ▼                    ▼
    Spring Security       Redis Cache        Rate Limiter
       JWT Auth            (Upstash)          Bucket4j
          │
          ▼
      Service Layer
          │
          ▼
   Spring Data JPA
          │
          ▼
     PostgreSQL Database
          ▲
          │
      Flyway Migrations
```

The application follows a layered architecture:
- Controllers expose REST APIs.
- Services contain business logic.
- Spring Data JPA handles persistence.
- PostgreSQL stores application data.
- Redis caches frequently accessed URLs.
- Flyway manages database schema migrations.
- Spring Security secures protected endpoints using JWT.