# API Documentation

Base URL

```
https://url-shortener-backend-qozg.onrender.com
```

---

# Authentication

## Register

POST `/api/auth/register`

Request

```json
{
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

Response

```json
{
  "success": true,
  "message": "Registration successful"
}
```

---

## Login

POST `/api/auth/login`

Request

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

Response

```json
{
  "token": "<JWT_TOKEN>",
  "tokenType": "Bearer"
}
```

---

# URL Management

## Create Short URL

POST `/api/v1/urls`

Authorization:

```
Bearer <JWT>
```

Request

```json
{
  "originalUrl": "https://spring.io",
  "customAlias": "spring"
}
```

---

## Get My URLs

GET `/api/v1/urls`

---

## Update URL Status

PATCH `/api/v1/urls/{id}/status`

---

## Delete URL

DELETE `/api/v1/urls/{id}`

---

# Redirect

GET `/{shortCode}`

Returns HTTP **302 Found** and redirects to the original URL.

---

# Admin

Requires `ROLE_ADMIN`

GET `/api/v1/admin/dashboard`

GET `/api/v1/admin/users`

GET `/api/v1/admin/urls`