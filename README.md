# 🚀 URL Shortener Service

A high-performance, production-ready **URL Shortener API** built with
**Spring Boot**, featuring authentication, caching, rate limiting,
Docker deployment, and load testing.

------------------------------------------------------------------------

## 📌 Features

### 🔗 Core Functionality

-   Create short URLs
-   Custom alias support
-   URL redirection
-   Expiration support
-   Collision handling
-   Pagination for URL listing

### 🔐 Security

-   JWT-based authentication
-   Role-based access control
-   Secure endpoints
-   Global exception handling

### ⚡ Performance & Scalability

-   Redis caching
-   Bucket4j rate limiting
-   Snowflake ID generation
-   Base62 encoding
-   Load tested with k6

### 🐳 DevOps Ready

-   Dockerized application
-   Docker Compose setup (App + MySQL + Redis)
-   Spring Boot Actuator enabled
-   Health, metrics, and environment endpoints

------------------------------------------------------------------------

# 🏗️ Architecture

Client ↓ Spring Boot REST API ↓ JWT Authentication ↓ Rate Limiter
(Bucket4j) ↓ Redis Cache ↓ MySQL Database

------------------------------------------------------------------------

# 🛠️ Tech Stack

  Layer              Technology
  ------------------ -------------------------
  Backend            Spring Boot
  Security           Spring Security + JWT
  Database           MySQL
  Cache              Redis
  Rate Limiting      Bucket4j
  Build Tool         Maven
  Containerization   Docker & Docker Compose
  Load Testing       k6

------------------------------------------------------------------------

# ⚙️ Running the Project

## 🔹 1. Clone Repository

``` bash
git clone https://github.com/your-username/urlshortener.git
cd urlshortener
```

------------------------------------------------------------------------

## 🔹 2. Build the Application

``` bash
mvn clean package
```

------------------------------------------------------------------------

## 🔹 3. Run with Docker

``` bash
docker compose up --build
```

Application runs at:

    http://localhost:8080

------------------------------------------------------------------------

# 📡 API Endpoints

## 🔑 Authentication

    POST /api/v1/auth/login

------------------------------------------------------------------------

## 🔗 Create Short URL

    POST /api/v1/urls

Body:

``` json
{
  "originalUrl": "https://example.com",
  "customAlias": "myalias",
  "expiresAt": "2026-12-31T00:00:00"
}
```

------------------------------------------------------------------------

## 🔄 Redirect

    GET /{shortCode}

Redirects to original URL.

------------------------------------------------------------------------

## 📄 List URLs (Paginated)

    GET /api/v1/urls?page=0&size=10

------------------------------------------------------------------------

# ⚡ Performance Testing

Load tested using **k6**.

### Example Test

``` bash
k6 run loadtest.js
```

### Sample Results (20 VUs, 30s)

-   \~28 requests/sec
-   \~200ms average response time
-   Rate limiter successfully protects system
-   Stable performance under concurrent load

------------------------------------------------------------------------

# 🛡️ Rate Limiting Strategy

-   Token Bucket algorithm (Bucket4j)
-   Per-IP rate limiting
-   Burst support
-   Configurable refill rate
-   Prevents abuse and protects backend resources

------------------------------------------------------------------------

# 📊 Observability

Spring Boot Actuator enabled:

-   `/actuator/health`
-   `/actuator/info`
-   `/actuator/metrics`
-   `/actuator/env`

------------------------------------------------------------------------

# 🐳 Docker Services

Docker Compose includes:

-   `app` (Spring Boot)
-   `mysql`
-   `redis`

------------------------------------------------------------------------

# 🔥 Production-Ready Characteristics

✔ Stateless API\
✔ Scalable design\
✔ Redis-backed caching\
✔ Rate-limited endpoints\
✔ Docker deployment\
✔ Load-tested under concurrency\
✔ Clean layered architecture

------------------------------------------------------------------------

# 📈 Future Improvements (Optional Enhancements)

-   Distributed Redis-backed rate limiter
-   Prometheus + Grafana monitoring
-   CI/CD pipeline
-   Kubernetes deployment
-   URL analytics dashboard
-   API documentation with Swagger

------------------------------------------------------------------------

# 👨‍💻 Author

Mishal\
Backend Developer \| Java & Spring Boot

------------------------------------------------------------------------

# 🏁 Project Status

✅ Complete (v1.0)\
🚀 Production-style backend service\
📦 Portfolio-ready
