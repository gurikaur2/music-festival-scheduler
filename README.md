# 🎵 Music Festival Scheduler (Backend)

## 📌 Project Status

✅ Core functionality completed
⚠️ Unit testing in progress (minor issues under resolution)

---

## 🧠 Problem Statement

A backend system to manage scheduling of performances in a multi-stage, multi-day music festival.

The system ensures:

* No overlapping performances on the same stage
* No artist is double-booked

---

## 🏗️ Features Implemented

### 🎤 Entity Modeling

* Artist (name, genre)
* Stage (name, description)
* Performance (date, startTime, endTime)

### 🔗 Relationships

* One Stage → Many Performances
* One Artist → Many Performances

---

### ⚙️ Core Functionality

* Add new performance with conflict validation
* Update existing performance
* Delete (cancel) performance
* Retrieve performances:

  * By date
  * By stage
  * Full schedule

---

### 🚫 Conflict Detection (Key Logic)

* Prevents overlapping performances on the same stage
* Prevents an artist from being scheduled at multiple places at the same time

---

### 🌐 REST API Layer

* Controller layer for all endpoints
* Service layer for business logic
* Repository layer using Spring Data JPA

---

### ⚠️ Exception Handling

* Global exception handler implemented
* Custom exceptions:

  * ConflictException
  * ResourceNotFoundException
* Proper HTTP responses (400, 404, 201)

---

### 🔍 Domain-Specific Feature

* Structured schedule retrieval:

  * Grouped by stage
  * Sorted by start time
* Endpoint for full festival schedule

---

### 🔁 AOP (Aspect-Oriented Programming)

* Logging aspect for:

  * Add / update / delete operations
* Rule-check logging (e.g., scheduling outside allowed time)

---

### 📄 API Documentation

* Swagger / OpenAPI integrated
* Interactive API testing via Swagger UI

---

## ⚙️ Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Database
* Maven

---

## 🧪 Testing Status

* Unit tests implemented for scheduling logic
* Conflict detection tests partially working
* Some edge cases under debugging

---

## 🛠️ How to Run

```bash
mvn spring-boot:run
```

---

## 📌 API Access

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## 🚀 Future Improvements

* Fix remaining unit test issues
* Add MySQL/PostgreSQL support
* Enhance validation and edge case handling
* Improve logging and monitoring

---

## 📅 Key Learnings

* Handling real-world scheduling conflicts
* Designing layered backend architecture
* Using AOP for cross-cutting concerns
* Implementing robust exception handling

---

## 👤 Author

Gurnoor Kaur
