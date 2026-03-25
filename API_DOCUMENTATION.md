# College Complaint Management System - REST API

A production-ready REST API backend for managing college complaints using Java Spring Boot, Spring Data JPA, and MySQL.

## Features

- **JWT-based Authentication** - Secure token-based authentication
- **Role-Based Access Control** - STUDENT and ADMIN roles with different permissions
- **RESTful API** - Complete CRUD operations for complaints
- **Pagination Support** - Efficient data retrieval with pagination
- **Input Validation** - Comprehensive validation using Spring validation annotations
- **Global Exception Handling** - Centralized error handling with meaningful HTTP status codes
- **Database Integration** - MySQL with JPA/Hibernate ORM
- **Clean Architecture** - Layered architecture with separation of concerns

## Project Structure

```
src/main/java/com/example/demo/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── ComplaintController.java
│   ├── AdminController.java
│   └── UserController.java
├── service/
│   ├── AuthService.java
│   ├── ComplaintService.java
│   ├── UserService.java
│   └── impl/
│       ├── AuthServiceImpl.java
│       ├── ComplaintServiceImpl.java
│       └── UserServiceImpl.java
├── repository/
│   ├── UserRepository.java
│   └── ComplaintRepository.java
├── model/entity/
│   ├── User.java
│   ├── Complaint.java
│   ├── Role.java
│   ├── ComplaintStatus.java
│   └── ComplaintCategory.java
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── CreateComplaintRequest.java
│   ├── UpdateComplaintRequest.java
│   ├── UpdateComplaintStatusRequest.java
│   ├── ComplaintResponse.java
│   ├── UserResponse.java
│   └── PagedResponse.java
├── security/
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── AppException.java
│   └── ErrorResponse.java
└── DemoApplication.java
```

## Prerequisites

- Java 21
- MySQL 8.0+
- Maven 3.6+
- Postman or similar tool for API testing

## Setup Instructions

### 1. Database Setup

Create MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS complaintBox CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configure MySQL Connection

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/complaintBox
spring.datasource.username=root
spring.datasource.password=Root@1234
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication Endpoints

#### 1. Register User
**POST** `/api/auth/register`

Request Body:
```json
{
  "name": "John Doe",
  "email": "john@college.edu",
  "password": "SecurePassword123"
}
```

Response (201 Created):
```json
"User registered successfully"
```

#### 2. Login
**POST** `/api/auth/login`

Request Body:
```json
{
  "email": "john@college.edu",
  "password": "SecurePassword123"
}
```

Response (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huQGNvbGxlZ2UuZWR1IiwiaWF0IjoxNjQzNzIwNDAwLCJleHAiOjE2NDM4MDY4MDB9.xxx",
  "type": "Bearer",
  "id": 1,
  "name": "John Doe",
  "email": "john@college.edu",
  "role": "STUDENT"
}
```

### User Endpoints

#### 3. Get Current User
**GET** `/api/users/me`

Headers:
```
Authorization: Bearer {token}
```

Response (200 OK):
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@college.edu",
  "role": "STUDENT",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### 4. Get User by ID
**GET** `/api/users/{id}`

Headers:
```
Authorization: Bearer {token}
```

Response (200 OK):
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@college.edu",
  "role": "STUDENT",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Student Complaint Endpoints

#### 5. Create Complaint
**POST** `/api/complaints`

Headers:
```
Authorization: Bearer {token}
Content-Type: application/json
```

Request Body:
```json
{
  "title": "Poor classroom infrastructure",
  "description": "The classroom lacks proper ventilation and lighting. Please look into this matter urgently.",
  "category": "INFRASTRUCTURE"
}
```

Response (201 Created):
```json
{
  "id": 1,
  "title": "Poor classroom infrastructure",
  "description": "The classroom lacks proper ventilation and lighting. Please look into this matter urgently.",
  "category": "INFRASTRUCTURE",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T10:35:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john@college.edu"
}
```

#### 6. Get My Complaints
**GET** `/api/complaints/my?page=0&size=10`

Headers:
```
Authorization: Bearer {token}
```

Response (200 OK):
```json
{
  "content": [
    {
      "id": 1,
      "title": "Poor classroom infrastructure",
      "description": "The classroom lacks proper ventilation and lighting. Please look into this matter urgently.",
      "category": "INFRASTRUCTURE",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:35:00",
      "updatedAt": "2024-01-15T10:35:00",
      "userId": 1,
      "userName": "John Doe",
      "userEmail": "john@college.edu"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

#### 7. Get Complaint by ID
**GET** `/api/complaints/{id}`

Headers:
```
Authorization: Bearer {token}
```

Response (200 OK):
```json
{
  "id": 1,
  "title": "Poor classroom infrastructure",
  "description": "The classroom lacks proper ventilation and lighting. Please look into this matter urgently.",
  "category": "INFRASTRUCTURE",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T10:35:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john@college.edu"
}
```

#### 8. Update Complaint
**PUT** `/api/complaints/{id}`

Headers:
```
Authorization: Bearer {token}
Content-Type: application/json
```

Request Body:
```json
{
  "title": "Poor classroom infrastructure - Urgent",
  "description": "The classroom lacks proper ventilation and lighting. This is affecting students' health. Please look into this matter urgently.",
  "category": "INFRASTRUCTURE"
}
```

Response (200 OK):
```json
{
  "id": 1,
  "title": "Poor classroom infrastructure - Urgent",
  "description": "The classroom lacks proper ventilation and lighting. This is affecting students' health. Please look into this matter urgently.",
  "category": "INFRASTRUCTURE",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T11:00:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john@college.edu"
}
```

#### 9. Delete Complaint
**DELETE** `/api/complaints/{id}`

Headers:
```
Authorization: Bearer {token}
```

Response (200 OK):
```json
"Complaint deleted successfully"
```

### Admin Endpoints

#### 10. Get All Complaints
**GET** `/api/admin/complaints?page=0&size=10`

Headers:
```
Authorization: Bearer {admin_token}
```

Response (200 OK):
```json
{
  "content": [
    {
      "id": 1,
      "title": "Poor classroom infrastructure",
      "description": "The classroom lacks proper ventilation and lighting. Please look into this matter urgently.",
      "category": "INFRASTRUCTURE",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:35:00",
      "updatedAt": "2024-01-15T10:35:00",
      "userId": 1,
      "userName": "John Doe",
      "userEmail": "john@college.edu"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

#### 11. Update Complaint Status
**PUT** `/api/admin/complaints/{id}/status`

Headers:
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```

Request Body:
```json
{
  "status": "IN_PROGRESS"
}
```

Response (200 OK):
```json
{
  "id": 1,
  "title": "Poor classroom infrastructure",
  "description": "The classroom lacks proper ventilation and lighting. Please look into this matter urgently.",
  "category": "INFRASTRUCTURE",
  "status": "IN_PROGRESS",
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T11:05:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john@college.edu"
}
```

#### 12. Delete Complaint (Admin)
**DELETE** `/api/admin/complaints/{id}`

Headers:
```
Authorization: Bearer {admin_token}
```

Response (200 OK):
```json
"Complaint deleted successfully"
```

## Complaint Categories

- ACADEMIC
- INFRASTRUCTURE
- FACULTY_CONDUCT
- ADMINISTRATIVE
- STUDENT_LIFE
- SAFETY
- OTHER

## Complaint Status

- PENDING - Initial state when complaint is created
- IN_PROGRESS - Admin is working on the complaint
- RESOLVED - Complaint has been resolved

## Error Responses

### 400 Bad Request
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/complaints",
  "errors": {
    "title": "Title is required",
    "description": "Description must be between 10 and 2000 characters"
  }
}
```

### 401 Unauthorized
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/auth/login"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Complaint not found",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/complaints/999"
}
```

### 500 Internal Server Error
```json
{
  "status": 500,
  "message": "An internal server error occurred",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/complaints"
}
```

## Authentication Flow

1. **Register** - User creates account via `/api/auth/register`
2. **Login** - User logs in via `/api/auth/login` and receives JWT token
3. **Use Token** - Include token in `Authorization: Bearer {token}` header for all authenticated requests
4. **Token Expiry** - Token expires after 24 hours (configurable in application.properties)

## Configuration

### JWT Settings (application.properties)
```properties
app.jwtSecret=mySecretKeyForJWTTokenGenerationAndValidationWithMinimum32Characters
app.jwtExpirationInMs=86400000  # 24 hours in milliseconds
```

### Database Settings (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/complaintBox
spring.datasource.username=root
spring.datasource.password=Root@1234
spring.jpa.hibernate.ddl-auto=update
```

## Security Features

- **Password Encryption** - Passwords are encrypted using BCrypt
- **JWT Tokens** - Secure token-based authentication with HS512 algorithm
- **Role-Based Authorization** - Different endpoints for STUDENT and ADMIN roles
- **CORS Enabled** - Cross-Origin Resource Sharing enabled for all origins
- **Input Validation** - All inputs are validated before processing
- **Exception Handling** - Global exception handler for consistent error responses

## Dependencies

- Spring Boot 4.0.3
- Spring Data JPA
- Spring Security
- MySQL Connector Java
- JWT (JJWT 0.12.3)
- Lombok
- Jakarta Validation API

## Testing the API

### Using Postman

1. **Register a new user:**
   - POST http://localhost:8080/api/auth/register
   - Body (JSON): `{"name": "John", "email": "john@test.com", "password": "password123"}`

2. **Login:**
   - POST http://localhost:8080/api/auth/login
   - Body (JSON): `{"email": "john@test.com", "password": "password123"}`
   - Copy the `token` from response

3. **Create complaint:**
   - POST http://localhost:8080/api/complaints
   - Header: `Authorization: Bearer {token}`
   - Body (JSON):
     ```json
     {
       "title": "Test Complaint",
       "description": "This is a test complaint for the system",
       "category": "INFRASTRUCTURE"
     }
     ```

4. **Get all my complaints:**
   - GET http://localhost:8080/api/complaints/my
   - Header: `Authorization: Bearer {token}`

## Best Practices Implemented

1. **Clean Architecture** - Separation of concerns with controller, service, repository layers
2. **Dependency Injection** - Using Spring's DI for loose coupling
3. **Pagination** - Support for paginated responses to handle large datasets
4. **DTOs** - Data Transfer Objects for request/response validation
5. **Exception Handling** - Global exception handler with meaningful error messages
6. **Validation** - Input validation using Jakarta validation annotations
7. **Security** - JWT-based authentication and role-based authorization
8. **Lombok** - Reducing boilerplate code for getters, setters, constructors
9. **Transaction Management** - Using `@Transactional` for data consistency
10. **Proper HTTP Status Codes** - Using appropriate status codes for different scenarios

## Troubleshooting

### Database Connection Error
- Ensure MySQL is running
- Check database credentials in application.properties
- Verify database exists: `CREATE DATABASE complaintBox;`

### JWT Token Invalid
- Token has expired (24 hours default)
- Login again to get a new token
- Ensure token is passed with `Bearer ` prefix in Authorization header

### Port Already in Use
- Change `server.port` in application.properties (default: 8080)
- Or kill the process using port 8080

## Future Enhancements

1. Email notifications when complaint status changes
2. File attachments for complaints
3. Comments/updates on complaints
4. Complaint analytics and reports
5. Rating system for complaint resolution
6. Bulk complaint import/export
7. Advanced filtering and search
8. Real-time notifications using WebSocket
9. Two-factor authentication
10. API rate limiting

## Support

For issues or questions, please create an issue in the repository or contact the development team.

## License

This project is licensed under the MIT License.
