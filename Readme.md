# Screenverse Backend

A Spring Boot REST API backend for Screenverse, a movie and TV show management platform that integrates with the WatchMode API to provide comprehensive entertainment content information.

## 🎬 Features

- **User Authentication**: Integration with Clerk for secure user authentication and management
- **Content Search**: Search for movies and TV shows using the WatchMode API
- **Watchlist Management**: Add, update, and track content in your personal watchlist
- **Rating System**: Rate and review watched content with a 1-5 star rating system
- **User Profiles**: Manage user information and preferences
- **RESTful API**: Clean, well-documented REST endpoints
- **Database Management**: MySQL database with Flyway migrations
- **Security**: JWT-based authentication with Spring Security

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL** - Database
- **Flyway** - Database migrations
- **Lombok** - Code generation
- **ModelMapper** - Object mapping
- **Auth0 JWT** - Token management
- **Maven** - Build tool

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Java 17** or higher
- **Maven 3.6** or higher
- **MySQL 8.0** or higher
- **WatchMode API Key** (get it from [WatchMode](https://www.watchmode.com/))

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd backend
```

### 2. Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE screenverse;
```

2. Update the database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Environment Variables

Set the following environment variable:
```bash
export WATCHMODE_API_KEY=your_watchmode_api_key
```

### 4. Run the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Authentication Endpoints

#### POST `/auth/signup`
Register a new user with Clerk authentication.

**Request Body:**
```json
{
  "clerkUserId": "user_123",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### POST `/auth/clerk/signin`
Sign in with Clerk authentication.

#### POST `/auth/refresh`
Refresh JWT token.

**Request Body:**
```json
{
  "refreshToken": "your_refresh_token"
}
```

#### GET `/auth/check-user`
Check if a user exists by email or Clerk user ID.

**Query Parameters:**
- `email` (optional): User's email
- `clerkUserId` (optional): Clerk user ID

### WatchMode API Endpoints

#### GET `/api/search`
Search for movies and TV shows.

**Query Parameters:**
- `searchValue` (required): Search term (e.g., "Breaking Bad")
- `searchField` (optional): Search field (default: "name")

**Example:**
```
GET /api/search?searchValue=Breaking Bad
```

#### GET `/api/title/{titleId}`
Get detailed information about a specific title.

**Example:**
```
GET /api/title/12345
```

### Watchlist Endpoints

All watchlist endpoints require authentication.

#### GET `/api/watchlist`
Get all watchlist items for the authenticated user.

#### POST `/api/watchlist`
Add a title to the watchlist.

**Request Body:**
```json
{
  "titleId": 12345,
  "name": "Breaking Bad",
  "plotOverview": "A high school chemistry teacher...",
  "year": 2008,
  "type": "tv_series",
  "genreName": "Drama",
  "poster": "https://example.com/poster.jpg"
}
```

#### PUT `/api/watchlist/{id}`
Update a watchlist item.

#### DELETE `/api/watchlist/{id}`
Delete a watchlist item.

#### PATCH `/api/watchlist/{id}/watched`
Mark a watchlist item as watched or unwatched.

**Query Parameters:**
- `watched` (required): Boolean value

### Rated Content Endpoints

All rated endpoints require authentication.

#### GET `/api/rated`
Get all rated items for the authenticated user.

#### POST `/api/rated`
Rate a title.

**Request Body:**
```json
{
  "titleId": 12345,
  "name": "Breaking Bad",
  "rating": 4.5,
  "plotOverview": "A high school chemistry teacher...",
  "year": 2008,
  "type": "tv_series",
  "genreName": "Drama",
  "poster": "https://example.com/poster.jpg"
}
```

#### PUT `/api/rated/{id}`
Update a rated item.

#### DELETE `/api/rated/{id}`
Delete a rated item.

### User Management Endpoints

#### GET `/api/users/profile`
Get the authenticated user's profile.

#### PUT `/api/users/profile`
Update the authenticated user's profile.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com"
}
```

## 🗄️ Database Schema

### Users Table
- `id`: Primary key
- `clerk_user_id`: Unique Clerk user identifier
- `email`: User's email address
- `first_name`: User's first name
- `last_name`: User's last name
- `auth_provider`: Authentication provider

### Watchlist Table
- `id`: Primary key
- `user_id`: Foreign key to users table
- `title_id`: WatchMode title ID
- `name`: Title name
- `watched`: Whether the title has been watched
- `plot_overview`: Title description
- `year`: Release year
- `type`: Content type (movie, tv_series, etc.)
- `genre_name`: Genre
- `poster`: Poster image URL

### Rated Table
- `id`: Primary key
- `user_id`: Foreign key to users table
- `title_id`: WatchMode title ID
- `name`: Title name
- `watched`: Whether the title has been watched (default: true)
- `rating`: User rating (1.0 - 5.0)
- `plot_overview`: Title description
- `year`: Release year
- `type`: Content type
- `genre_name`: Genre
- `poster`: Poster image URL

## 🔧 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/screenverse
spring.datasource.username=root
spring.datasource.password=root

# WatchMode API Configuration
watchmode.api.key=${WATCHMODE_API_KEY}
watchmode.api.base-url=https://api.watchmode.com/v1

# JWT Configuration
api.security.token.secret=your_jwt_secret

# Server Configuration
server.address=0.0.0.0
```

## 🧪 Testing

Run the test suite:

```bash
./mvnw test
```

## 📦 Building

Build the application:

```bash
./mvnw clean package
```

The JAR file will be created in the `target/` directory.

## 🚀 Deployment

### Docker (Recommended)

1. Build the Docker image:
```bash
docker build -t screenverse-backend .
```

2. Run the container:
```bash
docker run -p 8080:8080 \
  -e WATCHMODE_API_KEY=your_api_key \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/screenverse \
  screenverse-backend
```

### Traditional Deployment

1. Build the JAR:
```bash
./mvnw clean package
```

2. Run the application:
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## 🔒 Security

- **JWT Authentication**: Secure token-based authentication
- **Clerk Integration**: Third-party authentication provider
- **Spring Security**: Comprehensive security framework
- **Input Validation**: Request validation using Bean Validation
- **CORS Configuration**: Configurable Cross-Origin Resource Sharing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/your-repo/issues) page
2. Create a new issue with detailed information
3. Contact the development team

## 🔄 Version History

- **v0.0.1-SNAPSHOT**: Initial release with basic functionality
  - User authentication with Clerk
  - WatchMode API integration
  - Watchlist management
  - Rating system
  - User profile management

---

**Screenverse Backend** - Your gateway to comprehensive entertainment content management! 🎬✨