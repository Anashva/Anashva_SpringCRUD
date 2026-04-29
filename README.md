# Student Management System — Spring Boot + JDBC + PostgreSQL

## Project Structure

```
student-jdbc/
├── pom.xml
└── src/
    └── main/
        ├── java/com/student/app/
        │   ├── StudentManagementApplication.java  ← Entry point
        │   ├── model/
        │   │   └── Student.java                   ← Entity (id, name, email, course)
        │   ├── repository/
        │   │   └── StudentRepository.java         ← JdbcTemplate SQL queries
        │   ├── service/
        │   │   └── StudentService.java            ← Business logic
        │   └── controller/
        │       └── StudentController.java         ← REST API endpoints
        └── resources/
            ├── application.properties             ← DB config
            └── schema.sql                         ← Auto-creates table
```

---

## Prerequisites

| Tool       | Version  | Download |
|------------|----------|----------|
| Java JDK   | 17+      | https://adoptium.net |
| Maven      | 3.8+     | https://maven.apache.org/download.cgi |
| PostgreSQL | 14+      | https://www.postgresql.org/download |

---

## Step 1 — Setup PostgreSQL Database

Open pgAdmin or psql terminal and run:

```sql
CREATE DATABASE studentdb;
```

> The `students` TABLE is created automatically when the app starts (via schema.sql).

---

## Step 2 — Configure Database Credentials

Open the file:
```
src/main/resources/application.properties
```

Update these lines with your PostgreSQL credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/studentdb
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE
```

---

## Step 3 — Run the Application

Open a terminal in the project root folder (`student-jdbc/`) and run:

```bash
mvn spring-boot:run
```

You should see:
```
Started StudentManagementApplication on port 8080
```

---

## Step 4 — Test the API

Use **Postman**, **curl**, or any REST client.

### Create a Student
```
POST http://localhost:8080/students
Content-Type: application/json

{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "course": "B.Tech"
}
```

### Get All Students
```
GET http://localhost:8080/students
```

### Get Student by ID
```
GET http://localhost:8080/students/1
```

### Update a Student
```
PUT http://localhost:8080/students/1
Content-Type: application/json

{
  "name": "Rahul Kumar",
  "email": "rahul.kumar@example.com",
  "course": "M.Tech"
}
```

### Delete a Student
```
DELETE http://localhost:8080/students/1
```

---

## API Reference

| Method   | Endpoint           | Description          | Status Codes     |
|----------|--------------------|----------------------|------------------|
| POST     | /students          | Create a student     | 201, 500         |
| GET      | /students          | Get all students     | 200              |
| GET      | /students/{id}     | Get student by ID    | 200, 404         |
| PUT      | /students/{id}     | Update a student     | 200, 404         |
| DELETE   | /students/{id}     | Delete a student     | 200, 404         |

---

## Architecture

```
HTTP Request
     │
     ▼
 Controller      ← Handles HTTP, returns ResponseEntity
     │
     ▼
  Service        ← Business logic, validation
     │
     ▼
 Repository      ← JdbcTemplate (manual SQL, no Hibernate)
     │
     ▼
 PostgreSQL DB
```
