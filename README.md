# 🎵 Music Festival Scheduler (Backend)

## 📌 Project Status

🚧 In Progress — Core backend structure implemented

## 🧠 Problem Statement

Backend system to manage scheduling of performances in a multi-stage, multi-day music festival.

The system ensures:

* No overlapping performances on the same stage
* No artist is double-booked

---

## 🏗️ Current Progress

### ✔️ Entity Layer

* Artist (name, genre)
* Stage (name, description)
* Performance (date, startTime, endTime)

### ✔️ Relationships

* One Stage → Many Performances
* One Artist → Many Performances

### ✔️ Layers Created

* Controller (API endpoints)
* Service (business logic)
* Repository (data access)
* Exception (global error handling structure)

---

## ⚙️ Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Database
* Maven

---

## 🚀 Upcoming Features

* Conflict detection (no overlapping schedules)
* REST APIs for performance management
* AOP logging (add/update/delete tracking)
* Swagger/OpenAPI documentation
* JWT Authentication (bonus)

---

## 🛠️ How to Run

```bash
mvn spring-boot:run
```

---

## 📅 Planned APIs

* Add performance
* Update / Delete performance
* Get schedule by date
* Get performances by stage

---

## 📌 Notes

This project is being developed incrementally as part of a backend systems assignment focusing on scheduling logic and system design.
