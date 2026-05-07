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

```
---

## 🧪 Testing

### Unit Testing
- Service layer tested using JUnit 5
- Dependencies mocked using Mockito

### API Testing
- Tested using Postman
- Validated:
  - JWT authentication
  - Scheduling logic
  - Role-based access control

---

## 🗄️ Database

- H2 in-memory database
- Console available at:
```
http://localhost:8080/h2-console
```

### Default Credentials:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(empty)*

---

## 📊 Logging

Implemented using AOP (Aspect-Oriented Programming):

- Logs every API request
- Tracks execution time
- Helps in debugging and monitoring

---

## ⚠️ Business Rules

- Performances must be scheduled only between:
  - **1st August 2025 – 3rd August 2025**
- A stage cannot have overlapping performances
- Each performance is linked to one artist and one stage

---

## ▶️ How to Run

```bash id="run_cmd_12"
git clone https://github.com/<your-username>/<repo-name>.git
cd <repo-name>
mvn spring-boot:run
```
---
## 👨‍💻 Author

- **Gurnoor Kaur**

---
## ⭐ Future Improvements

- Frontend UI (React / Angular)
- Pagination & filtering for schedules
- Redis caching for performance optimization
- Docker containerization
- CI/CD pipeline using GitHub Actions


