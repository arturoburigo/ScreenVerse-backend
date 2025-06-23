# ScreenVerse Backend

A robust backend application for movie and TV series management, built with Spring Boot, offering authentication, watchlists, ratings, and WatchMode API integration.

## 🚀 Technologies Used

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **MySQL** - Database
- **Flyway** - Database migrations
- **JWT (Auth0)** - Authentication tokens
- **Lombok** - Boilerplate reduction
- **ModelMapper** - Object mapping
- **Maven** - Dependency management

## 📋 Features

- ✅ Clerk authentication (Google/GitHub)
- ✅ User management
- ✅ Watchlist functionality
- ✅ Rating system
- ✅ WatchMode API integration
- ✅ Movie and TV series search
- ✅ JWT for stateless authentication

## 🛠️ Environment Setup

### Prerequisites

- Java 17+
- MySQL 8.0+
- Maven 3.6+
- WatchMode account (for API key)

### Installation

1. **Clone the repository:**
```bash
git clone <repository-url>
cd screenverse-backend
```

2. **Configure MySQL database:**
```sql
CREATE DATABASE screenverse;
```

3. **Set up environment variables:**
   Copy `.env_example` to `.env` and configure:
```env
WATCHMODE_API_KEY=your_watchmode_api_key_here
```

4. **Configure application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/screenverse
spring.datasource.username=your_username
spring.datasource.password=your_password
```

5. **Run the application:**
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`

## 📚 API Endpoints

### 🔐 Authentication (`/auth`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `POST` | `/auth/signup` | Create new account | ❌ |
| `POST` | `/auth/clerk/signin` | Login with Clerk | ❌ |
| `POST` | `/auth/clerk/auth` | Unified authentication | ❌ |
| `POST` | `/auth/refresh` | Refresh token | ❌ |
| `GET` | `/auth/check-user` | Check if user exists | ❌ |

#### Example - Signup/Signin:
```json
POST /auth/signup
Content-Type: application/json

{
  "clerkUserId": "user_123abc",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "authProvider": "google"
}
```

#### Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "isNewUser": true
}
```

### 👥 Users (`/users`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `GET` | `/users` | List all users | ✅ |
| `GET` | `/users/{id}` | Get user by ID | ✅ |
| `POST` | `/users` | Create user | ✅ |
| `PUT` | `/users/{id}` | Update user | ✅ |
| `DELETE` | `/users/{id}` | Delete user | ✅ |

### 📺 WatchMode API (`/api`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `GET` | `/api/search` | Search movies/TV series | ✅ |
| `GET` | `/api/title/{titleId}` | Get title details | ✅ |

#### Example - Search:
```bash
GET /api/search?searchValue=Breaking Bad&searchField=name
Authorization: Bearer <token>
```

#### Response:
```json
{
  "title_results": [
    {
      "resultType": "title",
      "id": 12345,
      "name": "Breaking Bad",
      "type": "tv_series",
      "year": 2008,
      "details": {
        "title": "Breaking Bad",
        "plot_overview": "A high school chemistry teacher...",
        "poster": "https://...",
        "user_rating": 9.5,
        "genres": [18, 80],
        "genre_names": ["Drama", "Crime"]
      }
    }
  ],
  "people_results": []
}
```

### 🎬 Watchlist (`/api/watchlist`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `GET` | `/api/watchlist` | List watchlist items | ✅ |
| `POST` | `/api/watchlist` | Add to watchlist | ✅ |
| `PUT` | `/api/watchlist/{id}` | Update watchlist item | ✅ |
| `DELETE` | `/api/watchlist/{id}` | Remove from watchlist | ✅ |
| `PATCH` | `/api/watchlist/{id}/watched` | Mark as watched | ✅ |

#### Example - Add to Watchlist:
```json
POST /api/watchlist
Authorization: Bearer <token>
Content-Type: application/json

{
  "titleId": 12345,
  "name": "Breaking Bad",
  "watched": false,
  "plotOverview": "A high school chemistry teacher...",
  "year": 2008,
  "type": "tv_series",
  "genreName": "Drama, Crime",
  "poster": "https://image.url"
}
```

### ⭐ Ratings (`/api/rated`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `GET` | `/api/rated` | List rated items | ✅ |
| `POST` | `/api/rated` | Rate title | ✅ |
| `PUT` | `/api/rated/{id}` | Update rating | ✅ |
| `DELETE` | `/api/rated/{id}` | Remove rating | ✅ |

#### Example - Rate Title:
```json
POST /api/rated
Authorization: Bearer <token>
Content-Type: application/json

{
  "titleId": 12345,
  "name": "Breaking Bad",
  "rating": 4.5,
  "watched": true,
  "plotOverview": "A high school chemistry teacher...",
  "year": 2008,
  "type": "tv_series",
  "genreName": "Drama, Crime",
  "poster": "https://image.url"
}
```

## 🔑 Authentication

### Required Headers

For authenticated routes, include the header:
```
Authorization: Bearer <access_token>
```

### Token Refresh

```json
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

## 🗄️ Database Structure

### `users` Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clerk_user_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    auth_provider VARCHAR(50)
);
```

### `watchlist` Table
```sql
CREATE TABLE watchlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    watched BOOLEAN DEFAULT FALSE,
    plot_overview TEXT,
    year INT,
    type VARCHAR(50),
    genre_name VARCHAR(255),
    poster VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### `rated` Table
```sql
CREATE TABLE rated (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    watched BOOLEAN DEFAULT TRUE,
    rating FLOAT NOT NULL,
    plot_overview TEXT,
    year INT,
    type VARCHAR(50),
    genre_name VARCHAR(255),
    poster VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id),
    CHECK (rating >= 1.0 AND rating <= 5.0)
);
```

## 📁 Project Structure

```
src/main/java/com/screenverse/backend/
├── controller/          # REST Controllers
│   ├── auth/           # Authentication
│   ├── users/          # Users
│   ├── watchlist/      # Watchlist
│   ├── rated/          # Ratings
│   └── watchmode/      # WatchMode API
├── domain/             # JPA Entities
│   ├── users/
│   ├── watchlist/
│   └── rated/
├── dto/                # Data Transfer Objects
├── repository/         # JPA Repositories
├── service/            # Business Logic
├── infra/              # Infrastructure Configuration
│   └── security/       # Security and JWT
└── exception/          # Custom Exceptions
```

## 🔧 Important Configurations

### Environment Variables

```properties
# WatchMode API
WATCHMODE_API_KEY=your_api_key

# JWT Secret
api.security.token.secret=your_jwt_secret

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/screenverse
spring.datasource.username=root
spring.datasource.password=root
```

### CORS

By default, the application accepts requests from any origin during development. For production, configure properly in `SecurityConfigurations.java`.

## 🚀 Deployment

### Docker (Future)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Maven Commands

```bash
# Compile
./mvnw compile

# Run tests
./mvnw test

# Generate JAR
./mvnw package

# Run application
./mvnw spring-boot:run
```

## 🐛 Error Handling

The application has a global exception handling system that returns standardized responses:

```json
{
  "timestamp": "2025-01-20T10:30:00",
  "message": "User already exists with email: user@example.com",
  "status": 409,
  "error": "USER_ALREADY_EXISTS",
  "action": "REDIRECT_TO_SIGNIN"
}
```

## 📊 API Response Codes

| Code | Description |
|------|-------------|
| `200` | Success |
| `201` | Created |
| `204` | No Content |
| `400` | Bad Request |
| `401` | Unauthorized |
| `403` | Forbidden |
| `404` | Not Found |
| `409` | Conflict |
| `500` | Internal Server Error |

## 🔒 Security Features

- **JWT Authentication** - Stateless authentication
- **Password Encoding** - BCrypt hashing
- **CORS Configuration** - Cross-origin resource sharing
- **Input Validation** - Request validation
- **SQL Injection Protection** - JPA parameterized queries

## 📝 Logging

The application uses Spring Boot default logging. For production, configure `logback-spring.xml` appropriately.

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=AuthServiceTest

# Run with coverage
./mvnw test jacoco:report
```

## 📖 API Documentation

### Postman Collection

Import the provided Postman collection to test all endpoints:

1. Set environment variables:
    - `baseUrl`: `http://localhost:8080`
    - `token`: Your JWT token

2. Test authentication flow:
    - Sign up → Get tokens
    - Use access token for protected routes
    - Refresh token when needed

### Swagger (Future Enhancement)

Add Swagger UI for interactive API documentation:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.0</version>
</dependency>
```

## 🤝 Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java naming conventions
- Write unit tests for new features
- Update documentation for API changes
- Use meaningful commit messages

## 📄 License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## 🎯 Roadmap

- [ ] Add Swagger documentation
- [ ] Implement Redis caching
- [ ] Add rate limiting
- [ ] Docker containerization
- [ ] CI/CD pipeline
- [ ] Email notifications
- [ ] Advanced search filters
- [ ] Social features (sharing lists)

---

**ScreenVerse Backend** - Built with ❤️ using Spring Boot