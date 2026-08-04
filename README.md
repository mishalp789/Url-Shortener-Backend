# 🔗 URL Shortener Backend

A production-ready URL Shortener REST API built with **Spring Boot 3**, featuring JWT authentication, Redis caching, Flyway database migrations, rate limiting, Docker, CI/CD, and cloud deployment.

---

## 🌐 Live Application

- **API Base URL:** https://url-shortener-backend-qozg.onrender.com
- **Swagger UI:** https://url-shortener-backend-qozg.onrender.com/swagger-ui/index.html

## ✨ Features

- User Registration & Login (JWT Authentication)
- URL Shortening
- Custom URL Alias
- URL Expiration
- URL Analytics
- Click Tracking
- Dashboard Statistics
- Admin Dashboard
- Redis Caching
- Rate Limiting
- Flyway Database Migration
- Docker Support
- GitHub Actions CI
- Cloud Deployment (Render)

---

## 🛠 Technology Stack

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| Cache | Redis (Upstash) |
| Migration | Flyway |
| Build Tool | Maven |
| Documentation | Swagger / OpenAPI |
| Containerization | Docker |
| CI/CD | GitHub Actions |
| Deployment | Render |

---

## 📂 Project Structure

```text
src
├── auth
├── admin
├── common
├── security
├── url
└── scheduler
```

---

## 📚 Documentation

- [Architecture](docs/architecture.md)
- [Database Design](docs/database.md)
- [API Documentation](docs/api.md)

JWT Bearer Token Authentication

Public APIs:

- Register
- Login
- Redirect URL

Protected APIs:

- URL Management
- Analytics
- Dashboard
- Admin

---

## 📖 API Documentation

Swagger:

https://url-shortener-backend-qozg.onrender.com/swagger-ui/index.html

---

## 🐳 Docker

Build

```bash
docker build -t url-shortener .
```

Run

```bash
docker compose up
```

---

## 🧪 Testing

Run all tests

```bash
mvn clean test
```

Generate JaCoCo Report

```bash
mvn verify
```

---

## 🚀 Deployment

Deployed using:

- Render
- PostgreSQL
- Upstash Redis

---

## 📈 Future Improvements

- QR Code Download
- Custom Domains
- Email Verification
- Refresh Tokens
- Prometheus Metrics
- Grafana Dashboard
- Kubernetes Deployment

---

## 👨‍💻 Author

**Muhammed Mishal**

GitHub:

https://github.com/mishalp789