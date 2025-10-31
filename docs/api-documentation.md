# API Documentation - SkillBridge

## Base URL
```
https://api.skillbridge.com/v1
```

## Authentication
All API endpoints except `/auth/login` and `/auth/register` require JWT authentication.

### Authentication Header
```
Authorization: Bearer <jwt_token>
```

## Endpoints

### Authentication

#### Login
```http
POST /auth/login
Content-Type: application/json

{
    "username": "string",
    "password": "string"
}

Response (200 OK):
{
    "token": "string",
    "refreshToken": "string",
    "user": {
        "id": "number",
        "username": "string",
        "role": "string"
    }
}
```

#### Register
```http
POST /auth/register
Content-Type: application/json

{
    "username": "string",
    "email": "string",
    "password": "string",
    "role": "string"
}

Response (201 Created):
{
    "id": "number",
    "username": "string",
    "email": "string",
    "role": "string"
}
```

### Users

#### Get User Profile
```http
GET /users/{id}
Authorization: Bearer <token>

Response (200 OK):
{
    "id": "number",
    "username": "string",
    "email": "string",
    "role": "string",
    "profile": {
        "firstName": "string",
        "lastName": "string",
        "phone": "string"
    }
}
```

#### Update User Profile
```http
PUT /users/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
    "firstName": "string",
    "lastName": "string",
    "phone": "string"
}

Response (200 OK):
{
    "id": "number",
    "username": "string",
    "profile": {
        "firstName": "string",
        "lastName": "string",
        "phone": "string"
    }
}
```

### Batches

#### Create Batch
```http
POST /batches
Authorization: Bearer <token>
Content-Type: application/json

{
    "name": "string",
    "startDate": "date",
    "endDate": "date",
    "batchType": "string",
    "batchCategory": "string",
    "trainerId": "number"
}

Response (201 Created):
{
    "id": "number",
    "name": "string",
    "startDate": "date",
    "endDate": "date",
    "batchType": "string",
    "batchCategory": "string",
    "trainer": {
        "id": "number",
        "name": "string"
    }
}
```

#### Get Batch Details
```http
GET /batches/{id}
Authorization: Bearer <token>

Response (200 OK):
{
    "id": "number",
    "name": "string",
    "startDate": "date",
    "endDate": "date",
    "batchType": "string",
    "batchCategory": "string",
    "trainer": {
        "id": "number",
        "name": "string"
    },
    "students": [
        {
            "id": "number",
            "name": "string"
        }
    ]
}
```

### Assessments

#### Create Assessment
```http
POST /assessments
Authorization: Bearer <token>
Content-Type: application/json

{
    "title": "string",
    "description": "string",
    "batchId": "number",
    "totalMarks": "number",
    "passingMarks": "number",
    "questions": [
        {
            "question": "string",
            "options": ["string"],
            "correctAnswer": "number",
            "marks": "number"
        }
    ]
}

Response (201 Created):
{
    "id": "number",
    "title": "string",
    "description": "string",
    "totalMarks": "number",
    "passingMarks": "number"
}
```

#### Submit Assessment
```http
POST /assessments/{id}/submit
Authorization: Bearer <token>
Content-Type: application/json

{
    "studentId": "number",
    "answers": [
        {
            "questionId": "number",
            "selectedOption": "number"
        }
    ]
}

Response (200 OK):
{
    "score": "number",
    "totalMarks": "number",
    "passed": "boolean",
    "feedback": "string"
}
```

### Learning Paths

#### Create Learning Path
```http
POST /learning-paths
Authorization: Bearer <token>
Content-Type: application/json

{
    "title": "string",
    "description": "string",
    "milestones": [
        {
            "title": "string",
            "description": "string",
            "order": "number"
        }
    ]
}

Response (201 Created):
{
    "id": "number",
    "title": "string",
    "description": "string",
    "milestones": [
        {
            "id": "number",
            "title": "string",
            "order": "number"
        }
    ]
}
```

### Resources

#### Upload Resource
```http
POST /resources
Authorization: Bearer <token>
Content-Type: multipart/form-data

{
    "file": "file",
    "batchId": "number",
    "type": "string",
    "description": "string"
}

Response (201 Created):
{
    "id": "number",
    "filename": "string",
    "url": "string",
    "type": "string",
    "description": "string"
}
```

## Error Responses

### 400 Bad Request
```json
{
    "error": "string",
    "message": "string",
    "timestamp": "string"
}
```

### 401 Unauthorized
```json
{
    "error": "Unauthorized",
    "message": "Invalid or expired token",
    "timestamp": "string"
}
```

### 403 Forbidden
```json
{
    "error": "Forbidden",
    "message": "Insufficient permissions",
    "timestamp": "string"
}
```

### 404 Not Found
```json
{
    "error": "Not Found",
    "message": "Resource not found",
    "timestamp": "string"
}
```

### 500 Internal Server Error
```json
{
    "error": "Internal Server Error",
    "message": "An unexpected error occurred",
    "timestamp": "string"
}
```