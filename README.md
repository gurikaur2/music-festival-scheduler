# 🎵 Music Festival Scheduler (Backend)

## 📌 Project Status

🚧 Currently in development — Entity modeling phase completed.

## 🧠 Problem Statement

Backend system to manage scheduling of performances across multiple stages in a multi-day music festival. The system ensures:

* No overlapping performances on the same stage
* No artist is double-booked

## 🏗️ Current Progress

✔️ Defined core entities:

* Artist
* Stage
* Performance

✔️ Established relationships:

* One Stage → Many Performances
* One Artist → Many Performances

## ⚙️ Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Database

## 🚀 Upcoming Features

* Conflict detection logic (no overlaps)
* REST APIs for scheduling
* AOP logging
* JWT authentication (bonus)
* Swagger documentation

## 🛠️ How to Run

```bash
mvn spring-boot:run
```

## 📅 Future Scope

* Full schedule API grouped by stage
* Role-based authentication (Organizer / Viewer)
* Unit testing for scheduling logic
