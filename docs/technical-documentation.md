# Technical Documentation - SkillBridge

## System Architecture

### 1. Backend Architecture

#### 1.1 Layer Structure
```
com.college.skillbridge/
├── config/          # Configuration classes
├── controllers/     # REST API endpoints
├── models/          # Domain entities
├── repositories/    # Data access layer
├── services/        # Business logic
├── dto/            # Data transfer objects
├── exception/      # Custom exceptions
├── security/       # Security configurations
└── utils/          # Utility classes
```

#### 1.2 Key Components

##### Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- Token refresh mechanism
- Password encryption using BCrypt

##### Caching System
- Caffeine cache implementation
- Custom caching annotations
- Cache invalidation strategies
- User-specific caching

##### Database Design
- PostgreSQL with Supabase
- Optimized queries
- Connection pooling
- Transaction management

### 2. API Design

#### 2.1 Authentication APIs
```
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh-token
POST /api/auth/logout
```

#### 2.2 User Management APIs
```
GET    /api/users
POST   /api/users
GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
```

#### 2.3 Batch Management APIs
```
GET    /api/batches
POST   /api/batches
GET    /api/batches/{id}
PUT    /api/batches/{id}
DELETE /api/batches/{id}
GET    /api/batches/{id}/students
POST   /api/batches/{id}/students
```

#### 2.4 Assessment APIs
```
GET    /api/assessments
POST   /api/assessments
GET    /api/assessments/{id}
PUT    /api/assessments/{id}
DELETE /api/assessments/{id}
POST   /api/assessments/{id}/submit
```

### 3. Database Schema

#### 3.1 Core Entities

##### User
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

##### Batch
```sql
CREATE TABLE batches (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    batch_type VARCHAR(50) NOT NULL,
    batch_category VARCHAR(50) NOT NULL,
    trainer_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

##### Assessment
```sql
CREATE TABLE assessments (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    batch_id BIGINT REFERENCES batches(id),
    total_marks INTEGER NOT NULL,
    passing_marks INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 4. Security Implementation

#### 4.1 JWT Configuration
```java
@Configuration
public class JwtConfig {
    @Value("${JWT_SECRET}")
    private String secret;
    
    @Value("${JWT_EXPIRATION}")
    private long expiration;
    
    // JWT token generation and validation configuration
}
```

#### 4.2 Security Filters
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Security filter chain configuration
    // CORS configuration
    // CSRF protection
    // Authentication provider setup
}
```

### 5. Caching Strategy

#### 5.1 Cache Configuration
```java
@Configuration
@EnableCaching
public class CacheConfig {
    // Caffeine cache setup
    // Cache key generators
    // TTL configuration
}
```

### 6. Performance Optimizations

#### 6.1 Database Optimizations
- Indexed frequently queried columns
- Pagination for large result sets
- Query optimization using EXPLAIN
- Connection pooling with HikariCP

#### 6.2 Application Optimizations
- Response compression
- Static resource caching
- Lazy loading of relationships
- N+1 query prevention

### 7. Error Handling

#### 7.1 Global Exception Handler
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Handle various exceptions
    // Return appropriate error responses
}
```

#### 7.2 Custom Exceptions
```java
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final String message;
}
```

### 8. Monitoring and Logging

#### 8.1 Metrics
- Request/response times
- Cache hit/miss rates
- Database connection pool stats
- Memory usage

#### 8.2 Logging
- Request/response logging
- Error logging
- Performance logging
- Security event logging

### 9. Testing Strategy

#### 9.1 Unit Tests
- Service layer tests
- Repository layer tests
- Controller layer tests

#### 9.2 Integration Tests
- API endpoint tests
- Database integration tests
- Security integration tests

### 10. Deployment

#### 10.1 Environment Setup
```properties
# Application Properties
spring.profiles.active=${SPRING_PROFILE:dev}
server.port=${PORT:8080}

# Database Configuration
spring.datasource.url=${SUPABASE_URL}
spring.datasource.username=${SUPABASE_USER}
spring.datasource.password=${SUPABASE_PASSWORD}

# JWT Configuration
JWT_SECRET=${JWT_SECRET}
JWT_EXPIRATION=${JWT_EXPIRATION:86400000}
```

#### 10.2 Build Process
```bash
# Build the application
./gradlew clean build

# Run tests
./gradlew test

# Start the application
./gradlew bootRun
```