# College Complaint Management System - Setup Guide

## Quick Start Guide

This guide will walk you through setting up and running the College Complaint Management System backend.

## System Requirements

- **Java Development Kit (JDK)** 21 or higher
- **MySQL Server** 8.0 or higher
- **Maven** 3.6.0 or higher
- **Git** (optional, for cloning repository)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## Step-by-Step Setup

### Step 1: Install Prerequisites

#### Install Java 21
```bash
# Windows
Download from: https://www.oracle.com/java/technologies/downloads/

# Verify installation
java -version
```

#### Install MySQL
```bash
# Windows
Download and install from: https://dev.mysql.com/downloads/mysql/

# Start MySQL Service
# Windows: mysql -u root -p
```

#### Install Maven
```bash
# Windows
Download from: https://maven.apache.org/download.cgi
Set M2_HOME environment variable

# Verify installation
mvn -version
```

### Step 2: Database Setup

#### Create Database and User

Open MySQL Command Line or MySQL Workbench and execute:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS complaintBox 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Verify creation
SHOW DATABASES;
```

### Step 3: Configure Application

#### Update Database Connection

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/complaintBox
spring.datasource.username=root
spring.datasource.password=Root@1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server.port=8080

# JWT Configuration
app.jwtSecret=mySecretKeyForJWTTokenGenerationAndValidationWithMinimum32Characters
app.jwtExpirationInMs=86400000
```

**Note:** Replace username and password with your MySQL credentials.

### Step 4: Build the Project

Navigate to project directory and run:

```bash
# Clean previous builds
mvn clean

# Install dependencies and build
mvn install

# Or use
mvn clean install
```

If build is successful, you'll see: `BUILD SUCCESS`

### Step 5: Run the Application

```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Using Java command (after building)
java -jar target/demo-0.0.1-SNAPSHOT.jar

# Option 3: Run from IDE
Right-click DemoApplication.java > Run As > Java Application
```

**Expected Output:**
```
Started DemoApplication in 5.234 seconds
Tomcat started on port(s): 8080
```

### Step 6: Verify Application is Running

Open your browser and navigate to:
```
http://localhost:8080/api/auth/login
```

You should see a response (even if error, means server is running).

## Database Schema

The application will automatically create tables on startup. Tables created:

### USERS Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('STUDENT', 'ADMIN') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### COMPLAINTS Table
```sql
CREATE TABLE complaints (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  description LONGTEXT NOT NULL,
  category ENUM('ACADEMIC', 'INFRASTRUCTURE', 'FACULTY_CONDUCT', 'ADMINISTRATIVE', 'STUDENT_LIFE', 'SAFETY', 'OTHER') NOT NULL,
  status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED') NOT NULL DEFAULT 'PENDING',
  user_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## Testing the API with Postman

### Step 1: Install Postman
Download from: https://www.postman.com/downloads/

### Step 2: Import API Collection
Create new requests in Postman as shown below.

### Step 3: Test Register Endpoint

**Request:**
- Method: POST
- URL: `http://localhost:8080/api/auth/register`
- Headers: `Content-Type: application/json`
- Body (JSON):
```json
{
  "name": "John Doe",
  "email": "john@college.edu",
  "password": "SecurePass123"
}
```

**Expected Response:** 201 Created
```json
"User registered successfully"
```

### Step 4: Test Login Endpoint

**Request:**
- Method: POST
- URL: `http://localhost:8080/api/auth/login`
- Headers: `Content-Type: application/json`
- Body (JSON):
```json
{
  "email": "john@college.edu",
  "password": "SecurePass123"
}
```

**Expected Response:** 200 OK
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "name": "John Doe",
  "email": "john@college.edu",
  "role": "STUDENT"
}
```

**Copy the token for next requests.**

### Step 5: Test Create Complaint Endpoint

**Request:**
- Method: POST
- URL: `http://localhost:8080/api/complaints`
- Headers:
  - `Content-Type: application/json`
  - `Authorization: Bearer {paste_token_here}`
- Body (JSON):
```json
{
  "title": "Poor Library Facilities",
  "description": "The library lacks sufficient computers for student study. Please provide more resources.",
  "category": "INFRASTRUCTURE"
}
```

**Expected Response:** 201 Created
```json
{
  "id": 1,
  "title": "Poor Library Facilities",
  "description": "The library lacks sufficient computers for student study. Please provide more resources.",
  "category": "INFRASTRUCTURE",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john@college.edu"
}
```

### Step 6: Test Get My Complaints Endpoint

**Request:**
- Method: GET
- URL: `http://localhost:8080/api/complaints/my?page=0&size=10`
- Headers:
  - `Authorization: Bearer {paste_token_here}`

**Expected Response:** 200 OK
```json
{
  "content": [
    {
      "id": 1,
      "title": "Poor Library Facilities",
      "description": "The library lacks sufficient computers for student study. Please provide more resources.",
      "category": "INFRASTRUCTURE",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00",
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

## Creating Admin User

To create an admin user for testing admin endpoints:

### Option 1: Using SQL
```sql
-- First create a user via API (STUDENT role)
-- Then update the role using SQL

UPDATE users 
SET role = 'ADMIN' 
WHERE email = 'admin@college.edu';

-- Verify
SELECT * FROM users;
```

### Option 2: Direct Database Insert
```sql
INSERT INTO users (name, email, password, role, created_at, updated_at) 
VALUES ('Admin User', 'admin@college.edu', '$2a$10$...', 'ADMIN', NOW(), NOW());
```

**Note:** Use the password hash from the API registration, or generate a new BCrypt hash.

## Common Issues and Solutions

### Issue 1: Port 8080 Already in Use

**Solution:**
```bash
# Find process using port 8080
# Windows PowerShell
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess

# Kill the process
Stop-Process -Id <PID> -Force

# Or change port in application.properties
server.port=8081
```

### Issue 2: MySQL Connection Error

**Solution:**
```
Error: com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

1. Ensure MySQL is running
   - Windows: Check Services > MySQL80
   - Linux: sudo systemctl start mysql

2. Verify credentials in application.properties
   - Check username and password
   - Ensure database exists: SHOW DATABASES;

3. Check MySQL is on correct port (default 3306)
```

### Issue 3: Database Does Not Exist

**Solution:**
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE complaintBox CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Verify
SHOW DATABASES;
```

### Issue 4: JWT Token Expired

**Solution:**
- Tokens expire after 24 hours (configurable)
- Login again to get a new token
- Update the `Authorization` header with new token

### Issue 5: Build Fails with Maven

**Solution:**
```bash
# Clear Maven cache
mvn clean

# Rebuild
mvn clean install

# Check Java version is 21
java -version

# Check Maven version
mvn -version
```

## IDE Setup

### IntelliJ IDEA

1. Open project: File > Open > Select project folder
2. Configure JDK: File > Project Structure > Project > SDK > Select Java 21
3. Enable Annotation Processing: 
   - Settings > Build, Execution, Deployment > Compiler > Annotation Processors
   - Enable annotation processing
4. Run application: Right-click DemoApplication > Run

### VS Code

1. Install Extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - REST Client

2. Create `.vscode/launch.json`:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot App",
      "request": "launch",
      "cwd": "${workspaceFolder}",
      "console": "integratedTerminal",
      "mainClass": "com.example.demo.DemoApplication",
      "projectName": "demo",
      "preLaunchTask": "maven: clean install"
    }
  ]
}
```

3. Run using terminal:
```bash
mvn spring-boot:run
```

## Debugging

### Enable Debug Mode

Edit `application.properties`:
```properties
logging.level.root=INFO
logging.level.com.example.demo=DEBUG
logging.level.org.springframework.security=DEBUG
```

### View SQL Queries

Edit `application.properties`:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE
```

## Performance Tuning

### Connection Pool Configuration

Add to `application.properties`:
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### JPA Query Optimization

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

## Next Steps

1. Review [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) for complete API reference
2. Explore the codebase and understand the architecture
3. Write unit and integration tests
4. Deploy to production using Docker and Kubernetes
5. Set up CI/CD pipeline with GitHub Actions

## Support and Help

For issues:
1. Check error logs in console
2. Review `application.properties` configuration
3. Verify database setup
4. Check API_DOCUMENTATION.md for endpoint details

## Additional Resources

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Spring Security: https://spring.io/projects/spring-security
- MySQL Documentation: https://dev.mysql.com/doc/
- JWT: https://jwt.io/

---

**Happy Coding!** 🚀
