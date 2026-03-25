# Sample API Requests - College Complaint Management System

This file contains example requests for all API endpoints. You can import these into Postman or use them with any REST client.

## Environment Variables

Create these variables in Postman for easy testing:

```
base_url = http://localhost:8080
student_token = (paste token from login response)
admin_token = (paste token from admin login response)
complaint_id = (paste complaint ID from create response)
user_id = (paste user ID from login response)
```

---

## 1. AUTHENTICATION ENDPOINTS

### Register Student User

```
POST {{base_url}}/api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@college.edu",
  "password": "SecurePassword123"
}
```

**Response Examples:**

Success (201):
```json
"User registered successfully"
```

Error - Email Already Exists (400):
```json
{
  "status": 400,
  "message": "Email already registered",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/auth/register"
}
```

---

### Register Admin User

```
POST {{base_url}}/api/auth/register
Content-Type: application/json

{
  "name": "Admin User",
  "email": "admin@college.edu",
  "password": "AdminPassword123"
}
```

**Note:** After registration, update user role to ADMIN in database:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@college.edu';
```

---

### Login User

```
POST {{base_url}}/api/auth/login
Content-Type: application/json

{
  "email": "john.doe@college.edu",
  "password": "SecurePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRvZUBjb2xsZWdlLmVkdSIsImlhdCI6MTcxMDQzMDgwMCwiZXhwIjoxNzEwNTE3MjAwfQ.xxx",
  "type": "Bearer",
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@college.edu",
  "role": "STUDENT"
}
```

---

### Login with Wrong Password

```
POST {{base_url}}/api/auth/login
Content-Type: application/json

{
  "email": "john.doe@college.edu",
  "password": "WrongPassword"
}
```

**Response (401 Unauthorized):**
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": "2024-01-15T10:35:00",
  "path": "/api/auth/login"
}
```

---

## 2. USER ENDPOINTS

### Get Current User Info

```
GET {{base_url}}/api/users/me
Authorization: Bearer {{student_token}}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@college.edu",
  "role": "STUDENT",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### Get User by ID

```
GET {{base_url}}/api/users/1
Authorization: Bearer {{student_token}}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@college.edu",
  "role": "STUDENT",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### Get Non-Existent User

```
GET {{base_url}}/api/users/999
Authorization: Bearer {{student_token}}
```

**Response (404 Not Found):**
```json
{
  "status": 404,
  "message": "User not found",
  "timestamp": "2024-01-15T10:35:00",
  "path": "/api/users/999"
}
```

---

## 3. STUDENT COMPLAINT ENDPOINTS

### Create Complaint - Academic Issue

```
POST {{base_url}}/api/complaints
Authorization: Bearer {{student_token}}
Content-Type: application/json

{
  "title": "Unfair Marking in Final Exam",
  "description": "I believe my final exam was marked unfairly. The evaluation criteria were not clear and my answers deserved better marks. Please review my answer sheet.",
  "category": "ACADEMIC"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Unfair Marking in Final Exam",
  "description": "I believe my final exam was marked unfairly. The evaluation criteria were not clear and my answers deserved better marks. Please review my answer sheet.",
  "category": "ACADEMIC",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T10:35:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john.doe@college.edu"
}
```

---

### Create Complaint - Infrastructure Issue

```
POST {{base_url}}/api/complaints
Authorization: Bearer {{student_token}}
Content-Type: application/json

{
  "title": "Poor Classroom Infrastructure",
  "description": "The classroom lacks proper ventilation and lighting. Also, some chairs are broken. Please arrange for repairs and maintenance.",
  "category": "INFRASTRUCTURE"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "title": "Poor Classroom Infrastructure",
  "description": "The classroom lacks proper ventilation and lighting. Also, some chairs are broken. Please arrange for repairs and maintenance.",
  "category": "INFRASTRUCTURE",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:40:00",
  "updatedAt": "2024-01-15T10:40:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john.doe@college.edu"
}
```

---

### Create Complaint - Faculty Conduct

```
POST {{base_url}}/api/complaints
Authorization: Bearer {{student_token}}
Content-Type: application/json

{
  "title": "Disrespectful Teacher Behavior",
  "description": "Professor ABC has been disrespectful and dismissive towards students in class. This has affected student morale and classroom environment.",
  "category": "FACULTY_CONDUCT"
}
```

---

### Create Complaint - Invalid Request (Validation Error)

```
POST {{base_url}}/api/complaints
Authorization: Bearer {{student_token}}
Content-Type: application/json

{
  "title": "Short",
  "description": "Too short",
  "category": "ACADEMIC"
}
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:45:00",
  "path": "/api/complaints",
  "errors": {
    "title": "Title must be between 5 and 200 characters",
    "description": "Description must be between 10 and 2000 characters"
  }
}
```

---

### Create Complaint - Safety Issue

```
POST {{base_url}}/api/complaints
Authorization: Bearer {{student_token}}
Content-Type: application/json

{
  "title": "Safety Hazard in Parking Lot",
  "description": "The parking lot lights are broken and there are uneven surfaces creating tripping hazards. This is a safety concern especially during evening hours.",
  "category": "SAFETY"
}
```

---

### Get My Complaints (First Page)

```
GET {{base_url}}/api/complaints/my?page=0&size=10
Authorization: Bearer {{student_token}}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Unfair Marking in Final Exam",
      "description": "I believe my final exam was marked unfairly...",
      "category": "ACADEMIC",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:35:00",
      "updatedAt": "2024-01-15T10:35:00",
      "userId": 1,
      "userName": "John Doe",
      "userEmail": "john.doe@college.edu"
    },
    {
      "id": 2,
      "title": "Poor Classroom Infrastructure",
      "description": "The classroom lacks proper ventilation...",
      "category": "INFRASTRUCTURE",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:40:00",
      "updatedAt": "2024-01-15T10:40:00",
      "userId": 1,
      "userName": "John Doe",
      "userEmail": "john.doe@college.edu"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
```

---

### Get My Complaints (Paginated - Second Page)

```
GET {{base_url}}/api/complaints/my?page=1&size=5
Authorization: Bearer {{student_token}}
```

---

### Get Specific Complaint by ID

```
GET {{base_url}}/api/complaints/1
Authorization: Bearer {{student_token}}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Unfair Marking in Final Exam",
  "description": "I believe my final exam was marked unfairly...",
  "category": "ACADEMIC",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T10:35:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john.doe@college.edu"
}
```

---

### Get Non-Existent Complaint

```
GET {{base_url}}/api/complaints/999
Authorization: Bearer {{student_token}}
```

**Response (404 Not Found):**
```json
{
  "status": 404,
  "message": "Complaint not found or you don't have permission",
  "timestamp": "2024-01-15T10:50:00",
  "path": "/api/complaints/999"
}
```

---

### Update Complaint

```
PUT {{base_url}}/api/complaints/1
Authorization: Bearer {{student_token}}
Content-Type: application/json

{
  "title": "Unfair Marking in Final Exam - URGENT",
  "description": "I believe my final exam was marked unfairly. The evaluation criteria were not clear and my answers deserved better marks. Please review my answer sheet urgently as this is affecting my academic standing.",
  "category": "ACADEMIC"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Unfair Marking in Final Exam - URGENT",
  "description": "I believe my final exam was marked unfairly. The evaluation criteria were not clear and my answers deserved better marks. Please review my answer sheet urgently as this is affecting my academic standing.",
  "category": "ACADEMIC",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:35:00",
  "updatedAt": "2024-01-15T10:55:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john.doe@college.edu"
}
```

---

### Update Complaint - Access Denied

```
GET {{base_url}}/api/complaints/2
Authorization: Bearer {{another_student_token}}
```

**Response (404 Not Found):**
```json
{
  "status": 404,
  "message": "Complaint not found or you don't have permission",
  "timestamp": "2024-01-15T11:00:00",
  "path": "/api/complaints/2"
}
```

---

### Delete Complaint

```
DELETE {{base_url}}/api/complaints/1
Authorization: Bearer {{student_token}}
```

**Response (200 OK):**
```json
"Complaint deleted successfully"
```

---

## 4. ADMIN ENDPOINTS

### Get All Complaints

```
GET {{base_url}}/api/admin/complaints?page=0&size=10
Authorization: Bearer {{admin_token}}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 2,
      "title": "Poor Classroom Infrastructure",
      "description": "The classroom lacks proper ventilation...",
      "category": "INFRASTRUCTURE",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:40:00",
      "updatedAt": "2024-01-15T10:40:00",
      "userId": 1,
      "userName": "John Doe",
      "userEmail": "john.doe@college.edu"
    },
    {
      "id": 3,
      "title": "Disrespectful Teacher Behavior",
      "description": "Professor ABC has been disrespectful...",
      "category": "FACULTY_CONDUCT",
      "status": "PENDING",
      "createdAt": "2024-01-15T10:45:00",
      "updatedAt": "2024-01-15T10:45:00",
      "userId": 1,
      "userName": "John Doe",
      "userEmail": "john.doe@college.edu"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
```

---

### Update Complaint Status - To In Progress

```
PUT {{base_url}}/api/admin/complaints/2/status
Authorization: Bearer {{admin_token}}
Content-Type: application/json

{
  "status": "IN_PROGRESS"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "title": "Poor Classroom Infrastructure",
  "description": "The classroom lacks proper ventilation...",
  "category": "INFRASTRUCTURE",
  "status": "IN_PROGRESS",
  "createdAt": "2024-01-15T10:40:00",
  "updatedAt": "2024-01-15T11:05:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john.doe@college.edu"
}
```

---

### Update Complaint Status - To Resolved

```
PUT {{base_url}}/api/admin/complaints/2/status
Authorization: Bearer {{admin_token}}
Content-Type: application/json

{
  "status": "RESOLVED"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "title": "Poor Classroom Infrastructure",
  "description": "The classroom lacks proper ventilation...",
  "category": "INFRASTRUCTURE",
  "status": "RESOLVED",
  "createdAt": "2024-01-15T10:40:00",
  "updatedAt": "2024-01-15T11:10:00",
  "userId": 1,
  "userName": "John Doe",
  "userEmail": "john.doe@college.edu"
}
```

---

### Delete Complaint (Admin)

```
DELETE {{base_url}}/api/admin/complaints/3
Authorization: Bearer {{admin_token}}
```

**Response (200 OK):**
```json
"Complaint deleted successfully"
```

---

### Admin Access to Student Only Endpoint

```
GET {{base_url}}/api/complaints/my?page=0&size=10
Authorization: Bearer {{admin_token}}
```

**Response (200 OK):** Shows complaints where this admin user is the creator.

---

## 5. ERROR SCENARIOS

### Request Without Authorization Token

```
GET {{base_url}}/api/complaints/my
```

**Response (401 Unauthorized):**
```json
{
  "status": 401,
  "message": "Unauthorized",
  "timestamp": "2024-01-15T11:15:00",
  "path": "/api/complaints/my"
}
```

---

### Invalid Authorization Token

```
GET {{base_url}}/api/complaints/my
Authorization: Bearer invalid.token.here
```

**Response (401 Unauthorized):**
```json
{
  "status": 401,
  "message": "Unauthorized",
  "timestamp": "2024-01-15T11:20:00",
  "path": "/api/complaints/my"
}
```

---

### Student Trying to Access Admin Endpoint

```
GET {{base_url}}/api/admin/complaints
Authorization: Bearer {{student_token}}
```

**Response (403 Forbidden):**
```json
{
  "status": 403,
  "message": "Access Denied",
  "timestamp": "2024-01-15T11:25:00",
  "path": "/api/admin/complaints"
}
```

---

### Invalid JSON Format

```
POST {{base_url}}/api/complaints
Authorization: Bearer {{student_token}}
Content-Type: application/json

{
  "title": "Poor Classroom"
  "description": "No comma between fields"
}
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "message": "Invalid JSON format",
  "timestamp": "2024-01-15T11:30:00",
  "path": "/api/complaints"
}
```

---

## 6. POSTMAN COLLECTION JSON

You can import this JSON into Postman:

```json
{
  "info": {
    "name": "College Complaint Management System",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/api/auth/register"
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/api/auth/login"
          }
        }
      ]
    }
  ]
}
```

---

## Testing Tips

1. **Save responses** - Use Postman to save token values as environment variables
2. **Test pagination** - Try different page and size parameters
3. **Test validation** - Send invalid data to test error handling
4. **Test authorization** - Try accessing endpoints with wrong role
5. **Monitor database** - View actual data inserted using MySQL queries

---

**Happy Testing!** 🚀
