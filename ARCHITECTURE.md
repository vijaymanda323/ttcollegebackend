# College Complaint Management System - Architecture & Design

## System Overview

The College Complaint Management System is a production-ready REST API backend built with Spring Boot that enables students to submit complaints and admins to manage them. The system follows a clean, layered architecture with clear separation of concerns.

## Architecture Pattern

### Layered Architecture

```
┌─────────────────────────────────────┐
│      Presentation Layer             │
│      (REST Controllers)             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Service Layer                  │
│      (Business Logic)               │
├─────────────────────────────────────┤
│  - AuthService                      │
│  - ComplaintService                 │
│  - UserService                      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Repository Layer               │
│      (Data Access)                  │
├─────────────────────────────────────┤
│  - UserRepository                   │
│  - ComplaintRepository              │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Database Layer                 │
│      (MySQL + JPA/Hibernate)        │
└─────────────────────────────────────┘
```

## Component Description

### 1. Presentation Layer (Controllers)

**Purpose:** Handle HTTP requests and responses

**Components:**
- `AuthController` - Authentication endpoints (register, login)
- `ComplaintController` - Student complaint operations
- `AdminController` - Admin complaint management
- `UserController` - User information retrieval

**Responsibilities:**
- Parse HTTP requests
- Validate input parameters
- Call appropriate services
- Return HTTP responses with proper status codes
- Handle CORS

### 2. Service Layer

**Purpose:** Implement business logic and orchestrate operations

**Components:**
- `AuthService` & `AuthServiceImpl` - Handle user authentication
- `ComplaintService` & `ComplaintServiceImpl` - Manage complaints
- `UserService` & `UserServiceImpl` - Manage user data

**Responsibilities:**
- Apply business rules
- Validate data
- Transform DTOs to entities and vice versa
- Coordinate multiple repository calls
- Handle transactions

### 3. Repository Layer

**Purpose:** Provide database access abstraction

**Components:**
- `UserRepository` - User data access
- `ComplaintRepository` - Complaint data access

**Responsibilities:**
- Execute database queries
- Create, read, update, delete operations
- Provide custom query methods
- Manage transactions at data layer

### 4. Model/Entity Layer

**Purpose:** Represent domain objects

**Components:**
- `User` - User entity with role and account details
- `Complaint` - Complaint entity linked to users
- `ComplaintCategory` - Enum for complaint types
- `ComplaintStatus` - Enum for complaint states
- `Role` - Enum for user roles

**Relationships:**
```
User (1) ─────────────── (N) Complaint
   │
   ├─ id: Long
   ├─ name: String
   ├─ email: String
   ├─ password: String (encrypted)
   ├─ role: Role
   ├─ createdAt: LocalDateTime
   ├─ updatedAt: LocalDateTime
   └─ complaints: List<Complaint>

Complaint
   ├─ id: Long
   ├─ title: String
   ├─ description: String
   ├─ category: ComplaintCategory
   ├─ status: ComplaintStatus
   ├─ createdAt: LocalDateTime
   ├─ updatedAt: LocalDateTime
   └─ user: User (FK)
```

### 5. DTO Layer

**Purpose:** Transfer data between layers safely

**Components:**
- `RegisterRequest` - User registration data
- `LoginRequest` - User login credentials
- `LoginResponse` - Login response with token
- `CreateComplaintRequest` - New complaint data
- `UpdateComplaintRequest` - Complaint update data
- `UpdateComplaintStatusRequest` - Status change data
- `ComplaintResponse` - Complaint response data
- `UserResponse` - User information response
- `PagedResponse<T>` - Paginated response wrapper

**Benefits:**
- Hide entity structure from API
- Validate input data
- Control response data
- Prevent over-exposure of data

### 6. Security Layer

**Purpose:** Implement authentication and authorization

**Components:**
- `SecurityConfig` - Spring Security configuration
- `JwtTokenProvider` - JWT token generation and validation
- `JwtAuthenticationFilter` - JWT token filtering
- `CustomUserDetailsService` - Load user details from database

**Flow:**
```
User Login
    ↓
AuthController.login()
    ↓
AuthServiceImpl.login()
    ↓
AuthenticationManager.authenticate()
    ↓
CustomUserDetailsService.loadUserByUsername()
    ↓
JwtTokenProvider.generateToken()
    ↓
Return LoginResponse with JWT Token

Subsequent Requests:
    ↓
JwtAuthenticationFilter
    ↓
Extract & Validate Token
    ↓
Load UserDetails
    ↓
Set SecurityContext
    ↓
Allow Request to Proceed
```

### 7. Exception Handling

**Purpose:** Centralized error handling

**Components:**
- `GlobalExceptionHandler` - Centralized exception handler
- `ResourceNotFoundException` - Resource not found error
- `AppException` - General application error
- `ErrorResponse` - Structured error response

**Features:**
- Consistent error response format
- Meaningful error messages
- Proper HTTP status codes
- Request path in error response
- Validation error details

## Data Flow Examples

### User Registration Flow

```
POST /api/auth/register
    ↓
AuthController.register(RegisterRequest)
    ↓
Validation (using @Valid annotation)
    ↓
AuthServiceImpl.register()
    ├─ Check email exists
    ├─ Encode password using BCrypt
    ├─ Create User entity
    └─ Save to database
    ↓
Return 201 Created with success message
```

### Create Complaint Flow

```
POST /api/complaints
Authorization: Bearer {token}
Content-Type: application/json
Body: CreateComplaintRequest
    ↓
JwtAuthenticationFilter
    ├─ Extract token from header
    ├─ Validate token
    └─ Set SecurityContext
    ↓
AuthController checks @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    ↓
ComplaintController.createComplaint()
    ├─ Extract userId from Authentication
    ├─ Validate CreateComplaintRequest
    └─ Call ComplaintServiceImpl.createComplaint()
    ↓
ComplaintServiceImpl.createComplaint()
    ├─ Fetch User from database
    ├─ Create Complaint entity
    ├─ Set default status = PENDING
    ├─ Save to database
    └─ Return ComplaintResponse (DTO)
    ↓
Return 201 Created with Complaint details
```

### Get All Complaints (Admin)

```
GET /api/admin/complaints?page=0&size=10
Authorization: Bearer {admin_token}
    ↓
JwtAuthenticationFilter validates token
    ↓
AdminController checks @PreAuthorize("hasRole('ADMIN')")
    ↓
AdminController.getAllComplaints(page, size)
    ├─ Create Pageable from page and size
    └─ Call ComplaintServiceImpl.getAllComplaints()
    ↓
ComplaintServiceImpl.getAllComplaints()
    ├─ Query database with pagination
    ├─ Map Complaint entities to ComplaintResponse DTOs
    └─ Wrap in PagedResponse
    ↓
Return 200 OK with PagedResponse containing complaints
```

## HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful GET, PUT, DELETE |
| 201 | Created | Successful POST |
| 400 | Bad Request | Validation failure, invalid input |
| 401 | Unauthorized | Missing/invalid JWT token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 500 | Server Error | Unexpected server error |

## Security Considerations

### Authentication
- JWT tokens with HS512 algorithm
- Token expiry: 24 hours (configurable)
- Tokens must be sent as `Authorization: Bearer {token}`

### Authorization
- Role-based access control (RBAC)
- Two roles: STUDENT and ADMIN
- Method-level security using `@PreAuthorize`

### Password Security
- Passwords encrypted using BCrypt
- Never stored in plaintext
- Never returned in API responses

### Data Validation
- All inputs validated using Jakarta Validation
- Custom validation if needed
- Size limits on string fields
- Enum validation for categories and status

### CORS Protection
- Enabled for localhost:3000 for development
- Can be restricted in production

## Database Design

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_email (email)
);
```

### Complaints Table
```sql
CREATE TABLE complaints (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description LONGTEXT NOT NULL,
  category ENUM(
    'ACADEMIC', 
    'INFRASTRUCTURE', 
    'FACULTY_CONDUCT', 
    'ADMINISTRATIVE', 
    'STUDENT_LIFE', 
    'SAFETY', 
    'OTHER'
  ) NOT NULL,
  status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED') NOT NULL DEFAULT 'PENDING',
  user_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_status (status),
  INDEX idx_category (category)
);
```

## Design Patterns Used

### 1. Dependency Injection (DI)
- Spring's constructor injection
- Loose coupling between components
- Easy to test with mock objects

### 2. Repository Pattern
- Abstraction over data access
- Easier to switch databases
- Clean separation between business and data logic

### 3. Service Layer Pattern
- Business logic encapsulation
- Reusable across multiple controllers
- Testable in isolation

### 4. DTO Pattern
- Hide internal entity structure
- Validation at API boundary
- Control over exposed data

### 5. Facade Pattern
- Service layer as facade to repository layer
- Simplifies complex operations
- Coordinates multiple repositories

### 6. Singleton Pattern
- Services registered as singletons in Spring
- Efficient resource usage

### 7. Template Method Pattern
- JpaRepository provides base CRUD operations
- Extend with custom queries

## Performance Considerations

### Database Queries
- Use pagination to limit result set size
- Lazy loading for relationships
- Query indexes on frequently searched columns

### Caching
- Spring can cache results (not implemented but recommended)
- JWT token validation overhead minimal
- Database connection pooling configured

### Transaction Management
- Service layer manages transactions
- Read-only transactions for queries
- Implicit rollback on exceptions

## Error Handling Strategy

1. **Validation Errors** (400) - Input validation fails
2. **Authentication Errors** (401) - Missing/invalid credentials
3. **Authorization Errors** (403) - Insufficient permissions
4. **Not Found Errors** (404) - Resource doesn't exist
5. **Server Errors** (500) - Unexpected exceptions

All errors follow consistent JSON format with status, message, timestamp, and path.

## Testing Strategy Recommendations

### Unit Tests
- Service layer tests with mocked repositories
- DTO validation tests
- Security utility tests

### Integration Tests
- Controller tests with TestRestTemplate
- Service + Repository tests with TestDatabase
- Security tests

### Load Tests
- Test with realistic concurrent users
- Pagination stress tests
- Database connection pool sizing

## Scalability Considerations

### Horizontal Scaling
- Stateless API (JWT tokens)
- Database connection pooling
- Load balancer friendly

### Database Optimization
- Query indexes properly
- Archive old complaints
- Partition large tables

### Caching Strategy
- Cache user details after login
- Cache frequently accessed complaints
- Use Redis for distributed cache

## Future Enhancements

1. **Email Notifications** - Notify users on status changes
2. **File Attachments** - Upload supporting documents
3. **Complaint Comments** - Discussion threads
4. **Analytics Dashboard** - Complaint statistics
5. **Advanced Search** - Full-text search capabilities
6. **Rate Limiting** - API throttling per user
7. **Audit Logging** - Track all changes
8. **API Versioning** - Support multiple API versions
9. **WebSocket** - Real-time status updates
10. **Mobile API** - Optimized mobile endpoints

## Deployment Architecture

```
┌─────────────────────────────────────┐
│      Client Application             │
│      (Web/Mobile)                   │
└──────────────┬──────────────────────┘
               │
        ┌──────▼──────┐
        │  Load       │
        │  Balancer   │
        └──────┬──────┘
               │
    ┌──────────┼──────────┐
    │          │          │
┌───▼──┐  ┌───▼──┐  ┌───▼──┐
│API   │  │API   │  │API   │ (Multiple instances)
│Server│  │Server│  │Server│
│(Docker)  │(Docker)  │(Docker)
└────┬─┘  └────┬─┘  └────┬─┘
     │         │         │
     └────┬────┴────┬────┘
          │         │
      ┌───▼────┬────▼──┐
      │         │       │
    [MySQL]  [Redis]  [S3 Bucket]
   (Database) (Cache) (Files)
```

---

**This architecture provides a solid foundation for a production-ready REST API with room for growth and enhancement.**
