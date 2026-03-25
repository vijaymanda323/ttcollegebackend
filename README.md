# College Complaint Management System

A production-ready REST API backend for managing college student complaints built with Java Spring Boot, Spring Security, JWT, Spring Data JPA, and MySQL.

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![License](https://img.shields.io/badge/License-MIT-green)

## 📋 Table of Contents

- [Features](#-features)
- [System Requirements](#-system-requirements)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Configuration](#-configuration)
- [Authentication & Authorization](#-authentication--authorization)
- [Error Handling](#-error-handling)
- [Documentation](#-documentation)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

## ✨ Features

### Core Features
- ✅ **JWT-based Authentication** - Secure token-based user authentication
- ✅ **Role-Based Access Control** - STUDENT and ADMIN roles with different permissions
- ✅ **Complete CRUD Operations** - Full complaint lifecycle management
- ✅ **Pagination Support** - Efficient handling of large datasets
- ✅ **Input Validation** - Comprehensive validation using annotations
- ✅ **Global Exception Handling** - Centralized error handling with meaningful messages
- ✅ **Clean Architecture** - Layered architecture with separation of concerns
- ✅ **Database Integration** - MySQL with JPA/Hibernate ORM

### Security Features
- 🔒 **Password Encryption** - BCrypt password hashing
- 🔐 **JWT Tokens** - HS512 algorithm with 24-hour expiry
- 🛡️ **CORS Protection** - Configurable CORS settings
- ✔️ **Input Validation** - All inputs sanitized and validated
- 🚫 **Authorization Checks** - Method-level security annotations

### Developer Features
- 📦 **Lombok Integration** - Reduced boilerplate code
- 📚 **DTO Pattern** - Type-safe data transfer
- 🏗️ **Layered Architecture** - Clean separation of concerns
- 🧪 **Testable Design** - Dependency injection for easy testing
- 📖 **Comprehensive Documentation** - API docs and guides included

## 📦 System Requirements

| Tool | Version |
|------|---------|
| Java (JDK) | 21 or higher |
| MySQL | 8.0 or higher |
| Maven | 3.6.0 or higher |
| IDE | IntelliJ IDEA / Eclipse / VS Code |
| Git | 2.0+ (optional) |

## 🚀 Quick Start

### 1. Clone/Download the Project
```bash
# Navigate to project directory
cd CollegeComplaint/demo
```

### 2. Setup Database
```bash
# Open MySQL and create database
mysql -u root -p
CREATE DATABASE complaintBox CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Application
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/complaintBox
spring.datasource.username=root
spring.datasource.password=Root@1234
```

### 4. Build & Run
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Application starts on http://localhost:8080
```

### 5. Test API
```bash
# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@college.edu","password":"SecurePass123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@college.edu","password":"SecurePass123"}'
```

## 📁 Project Structure

```
src/main/java/com/example/demo/
│
├── config/
│   └── SecurityConfig.java           # Spring Security configuration
│
├── controller/
│   ├── AuthController.java           # Authentication endpoints
│   ├── ComplaintController.java       # Complaint endpoints
│   ├── AdminController.java           # Admin endpoints
│   └── UserController.java            # User endpoints
│
├── service/
│   ├── AuthService.java              # Authentication business logic
│   ├── ComplaintService.java          # Complaint business logic
│   ├── UserService.java               # User business logic
│   └── impl/
│       ├── AuthServiceImpl.java
│       ├── ComplaintServiceImpl.java
│       └── UserServiceImpl.java
│
├── repository/
│   ├── UserRepository.java            # User data access
│   └── ComplaintRepository.java        # Complaint data access
│
├── model/entity/
│   ├── User.java                      # User entity
│   ├── Complaint.java                 # Complaint entity
│   ├── Role.java                      # Role enum
│   ├── ComplaintStatus.java           # Status enum
│   └── ComplaintCategory.java         # Category enum
│
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── CreateComplaintRequest.java
│   ├── UpdateComplaintRequest.java
│   ├── ComplaintResponse.java
│   ├── UserResponse.java
│   └── PagedResponse.java
│
├── security/
│   ├── JwtTokenProvider.java          # JWT token management
│   ├── JwtAuthenticationFilter.java    # JWT filter
│   └── CustomUserDetailsService.java   # User details service
│
├── exception/
│   ├── GlobalExceptionHandler.java     # Global exception handling
│   ├── ResourceNotFoundException.java
│   ├── AppException.java
│   └── ErrorResponse.java
│
└── DemoApplication.java               # Main application class

resources/
├── application.properties               # Configuration
└── static/
    └── (static files, if any)
```

## 🔗 API Endpoints

### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | User login |

### User Endpoints
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/users/me` | Get current user | ✅ |
| GET | `/api/users/{id}` | Get user by ID | ✅ |

### Student Endpoints
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/complaints` | Create complaint | ✅ |
| GET | `/api/complaints/my` | Get my complaints | ✅ |
| GET | `/api/complaints/{id}` | Get complaint details | ✅ |
| PUT | `/api/complaints/{id}` | Update complaint | ✅ |
| DELETE | `/api/complaints/{id}` | Delete complaint | ✅ |

### Admin Endpoints
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/admin/complaints` | Get all complaints | ✅ ADMIN |
| PUT | `/api/admin/complaints/{id}/status` | Update status | ✅ ADMIN |
| DELETE | `/api/admin/complaints/{id}` | Delete complaint | ✅ ADMIN |

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('STUDENT', 'ADMIN') DEFAULT 'STUDENT',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Complaints Table
```sql
CREATE TABLE complaints (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description LONGTEXT NOT NULL,
  category ENUM('ACADEMIC', 'INFRASTRUCTURE', 'FACULTY_CONDUCT', 
                'ADMINISTRATIVE', 'STUDENT_LIFE', 'SAFETY', 'OTHER'),
  status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED') DEFAULT 'PENDING',
  user_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## ⚙️ Configuration

### application.properties
```properties
# Server
server.port=8080
spring.application.name=college-complaint-system

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/complaintBox
spring.datasource.username=root
spring.datasource.password=Root@1234
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
app.jwtSecret=mySecretKeyForJWTTokenGenerationAndValidationWithMinimum32Characters
app.jwtExpirationInMs=86400000

# Logging
logging.level.root=INFO
logging.level.com.example.demo=DEBUG
```

## 🔐 Authentication & Authorization

### Flow
1. User registers with email and password
2. User logs in with email and password
3. Server returns JWT token
4. Client includes token in `Authorization: Bearer {token}` header
5. Server validates token on each request

### Roles
- **STUDENT** - Can create, read, update, delete own complaints
- **ADMIN** - Can view all complaints and update their status

### Example Request with Token
```bash
curl -X GET http://localhost:8080/api/complaints/my \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## ❌ Error Handling

### Error Response Format
```json
{
  "status": 400,
  "message": "Error message",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/complaints",
  "errors": {
    "field": "error message"
  }
}
```

### Common Status Codes
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation error)
- `401` - Unauthorized (invalid token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `500` - Server Error

## 📚 Documentation

Complete documentation is available in the following files:

1. **[SETUP_GUIDE.md](./SETUP_GUIDE.md)** - Detailed setup and installation guide
2. **[API_DOCUMENTATION.md](./API_DOCUMENTATION.md)** - Complete API reference with examples
3. **[POSTMAN_EXAMPLES.md](./POSTMAN_EXAMPLES.md)** - Postman collection and example requests
4. **[ARCHITECTURE.md](./ARCHITECTURE.md)** - Architecture design and patterns
5. **[HELP.md](./HELP.md)** - Additional help and resources

## 🐛 Troubleshooting

### Database Connection Error
```
Error: Communications link failure

Solution:
1. Ensure MySQL is running
2. Check database URL and credentials
3. Verify database exists
```

### Port Already in Use
```
Error: Address already in use: 8080

Solution:
1. Change port in application.properties (server.port=8081)
2. Or kill process using port 8080
```

### JWT Token Invalid
```
Error: Invalid token

Solution:
1. Token may have expired (24 hours)
2. Login again to get new token
3. Include "Bearer " prefix in Authorization header
```

### Build Failure
```
Error: Build fails with Maven

Solution:
1. mvn clean (clear cache)
2. Verify Java 21 is installed
3. Check internet connection for dependency download
```

## 🧪 Testing

### Using Postman
1. Download [Postman](https://www.postman.com/downloads/)
2. Import collection from [POSTMAN_EXAMPLES.md](./POSTMAN_EXAMPLES.md)
3. Set environment variables (base_url, token)
4. Run requests and verify responses

### Using cURL
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","password":"pass123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@test.com","password":"pass123"}'

# Create complaint
curl -X POST http://localhost:8080/api/complaints \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"title":"Issue","description":"Details","category":"ACADEMIC"}'
```

## 📈 Performance

- **Pagination** - Limits result sets to prevent large data transfers
- **Lazy Loading** - Database relationships loaded on demand
- **Connection Pooling** - Efficient database connection management
- **Stateless Design** - Easy horizontal scaling

## 🔒 Security Best Practices

1. ✅ Passwords encrypted with BCrypt
2. ✅ JWT tokens with HS512 algorithm
3. ✅ Input validation on all endpoints
4. ✅ SQL injection protection via JPA
5. ✅ CORS protection enabled
6. ✅ Role-based authorization
7. ✅ Authorization checks on admin endpoints

## 🚀 Deployment

### Docker Deployment
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Environment Variables
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/complaintBox
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
APP_JWTSECRET=your-secret-key
```

## 📝 Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👥 Support

For issues, questions, or suggestions:
1. Check [TROUBLESHOOTING](#-troubleshooting) section
2. Review [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
3. Check [ARCHITECTURE.md](./ARCHITECTURE.md) for design details

## 🎓 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/projects/spring-security)
- [JWT Information](https://jwt.io/)
- [MySQL Tutorial](https://dev.mysql.com/doc/)
- [RESTful API Best Practices](https://restfulapi.net/)

## 🎯 Future Enhancements

- [ ] Email notifications on status changes
- [ ] File attachment support
- [ ] Complaint comments/discussions
- [ ] Analytics dashboard
- [ ] Advanced search with filters
- [ ] Rate limiting
- [ ] Audit logging
- [ ] API versioning
- [ ] WebSocket for real-time updates
- [ ] Mobile app API

---

**Made with ❤️ for College Complaint Management**

Last Updated: January 2024
