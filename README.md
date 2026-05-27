# Hirezo

Hirezo is a Spring Boot REST API for managing users, jobs and job applications.

## Key features
- User authentication (JWT)
- Role-based access (USER, ADMIN)
- CRUD for jobs and applications
- PostgreSQL persistence via Spring Data JPA
- Simple exception handling and DTO layering

## Requirements
- Java 17+
- Maven (or use the included Maven wrapper)
- PostgreSQL

## Configuration
Edit `src/main/resources/application.properties` to set your database and JWT secret.
Important properties:
- `spring.datasource.url` — JDBC URL for PostgreSQL
- `spring.datasource.username` — DB user
- `spring.datasource.password` — DB password
- `jwt.secret` — secret used to sign JWTs

Example (already present in repository):
```
spring.datasource.url=jdbc:postgresql://localhost:5432/hirezoflow
spring.datasource.username=postgres
spring.datasource.password=1234
jwt.secret=your-secret-here
```

## Run locally
Create the database and user in PostgreSQL first, then run:

On Windows:
```powershell
mvnw.cmd spring-boot:run
```
On Unix/macOS:
```bash
./mvnw spring-boot:run
```

Build and run the JAR:
```bash
./mvnw package
java -jar target/*.jar
```

Run tests:
```bash
./mvnw test
```

## Database
Create a PostgreSQL database named `hirezoflow` (or change `spring.datasource.url`). The app uses `spring.jpa.hibernate.ddl-auto=update` by default to create/update schema automatically.

## API
The API exposes endpoints under `/api` for jobs, users and applications. See the controller classes in `src/main/java/com/example/hirezo/controller` for exact routes and request/response DTOs.

## Notes
- For production, move secrets out of `application.properties` and use environment variables or a secret manager.
- Consider switching `spring.jpa.hibernate.ddl-auto` to `validate` or removing it in production.

## License
This repository currently contains no license file. Add a license if you plan to publish.
