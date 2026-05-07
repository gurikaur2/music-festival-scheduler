# 🎵 Music Festival Event Management System

A Spring Boot backend application for managing a music festival. It provides features for managing artists, stages, and performance scheduling with secure authentication, proper validation, and full test coverage.

---

## 🚀 Features

- 🎤 Manage Artists (Create, View)
- 🎪 Manage Stages
- 📅 Schedule Performances within festival dates (1–3 August 2025)
- 🔐 JWT Authentication & Role-based Authorization (Admin/Viewer)
- 📖 Swagger API Documentation
- 🧪 Unit Testing with JUnit & Mockito
- 🧭 AOP Logging for request tracing
- 💾 H2 In-Memory Database
- ❗ Global Exception Handling

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- H2 Database
- Maven
- Swagger (Springdoc OpenAPI)
- JUnit 5
- Mockito
- AOP (Aspect-Oriented Programming)

---

## 📁 Project Structure
com.gurnoor.music_event
├── controller
├── service
├── repository
├── model
├── security
├── exception
├── config
├── aspect


---

## 🔐 Security

- JWT-based authentication system
- Role-based access control (Admin / Viewer)
- Secure endpoints for creating/updating data
- Public access for schedule viewing

---

## 📌 API Endpoints

### 👤 Artist APIs
- `GET /api/artists`
- `POST /api/artists`

### 🎪 Stage APIs
- `GET /api/stages`

### 📅 Performance APIs
- `POST /api/performances`
- `GET /schedule/{date}`
- `GET /schedule/stages`

---

## 📖 Swagger UI

After running the application:

```

http://localhost:8080/swagger-ui/index.html
