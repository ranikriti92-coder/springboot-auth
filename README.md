# Spring Boot JWT Authentication & Authorization

A complete, production-ready REST API demonstrating JWT-based authentication and role-based authorization using Spring Boot 3, Spring Security 6, and H2 (easily swappable to MySQL/PostgreSQL).

---

## Tech Stack

| Layer | Tech |
|---|---|
| Framework | Spring Boot 3.2 |
| Security | Spring Security 6 + JWT (jjwt 0.11.5) |
| Database | H2 (in-memory) |
| ORM | Spring Data JPA / Hibernate |
| Java | 17+ |
| Build | Maven |

---

## Project Structure

```
src/main/java/com/example/auth/
├── AuthApplication.java          # Entry point
├── config/
│   ├── SecurityConfig.java       # Security filter chain, CORS, session
│   └── GlobalExceptionHandler.java
├── controller/
│   ├── AuthController.java       # /api/auth/login, /api/auth/register
│   └── TestController.java       # Role-protected demo endpoints
├── dto/
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── JwtResponse.java
│   └── MessageResponse.java
├── entity/
│   ├── User.java
│   └── Role.java                 # ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN
├── repository/
│   └── UserRepository.java
├── security/
│   ├── AuthEntryPointJwt.java    # 401 JSON response
│   ├── AuthTokenFilter.java      # Reads JWT from Authorization header
│   ├── JwtUtils.java             # Generate / validate / parse token
│   ├── UserDetailsImpl.java
│   └── UserDetailsServiceImpl.java
└── service/
    └── AuthService.java
```

---

## Running the App

```bash
mvn spring-boot:run
```

App starts on **http://localhost:8080**  
H2 Console: **http://localhost:8080/h2-console** (JDBC URL: `jdbc:h2:mem:authdb`)

---

## API Reference

### 1. Register

```
POST /api/auth/register
Content-Type: application/json

{
  "username": "john",
  "email": "john@example.com",
  "password": "mypassword",
  "roles": ["user"]          // optional: "user" | "mod" | "admin"
}
```

**Response 200**
```json
{ "message": "User registered successfully!" }
```

**Response 400** (duplicate username/email)
```json
{ "message": "Username is already taken!" }
```

---

### 2. Login

```
POST /api/auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "mypassword"
}
```

**Response 200**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

**Response 401** — bad credentials

---

### 3. Protected Endpoints (require JWT)

Add this header to all requests below:
```
Authorization: Bearer <your_token_here>
```

| Endpoint | Allowed Roles | Response |
|---|---|---|
| `GET /api/test/user` | USER, MOD, ADMIN | `Hello User!` |
| `GET /api/test/mod` | MOD, ADMIN | `Hello Moderator!` |
| `GET /api/test/admin` | ADMIN only | `Hello Admin!` |

**Response 401** — missing/invalid token  
**Response 403** — valid token but insufficient role

---

## cURL Examples

```bash
# Register an admin
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@example.com","password":"admin123","roles":["admin"]}'

# Login and capture token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

# Access admin-only endpoint
curl http://localhost:8080/api/test/admin \
  -H "Authorization: Bearer $TOKEN"
```

---

## Switching to MySQL / PostgreSQL

1. Replace the H2 dependency in `pom.xml` with your driver:
   ```xml
   <dependency>
     <groupId>com.mysql</groupId>
     <artifactId>mysql-connector-j</artifactId>
   </dependency>
   ```

2. Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/authdb
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```

---

## Security Notes

- Passwords are hashed with **BCrypt** (never stored in plaintext)
- JWTs are signed with **HS256** — change the secret in `application.properties` in production
- Sessions are **stateless** (STATELESS policy) — no server-side session state
- `@PreAuthorize` is used for fine-grained method-level authorization
- The `Authorization` header format is `Bearer <token>`
