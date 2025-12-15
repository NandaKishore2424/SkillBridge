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
- HTTP-only JWT cookies (short-lived access token + rotating refresh token)
- `TokenCookieService` issues/clears cookies; `JwtTokenFilter` reads tokens from headers or cookies
- Refresh tokens persisted in `refresh_token` table (one active per user) with automatic rotation
- Role-based access control (RBAC) enforced in `SecurityConfig` (`/api/v1/admin/**`, `/api/v1/trainers/**`, `/api/v1/students/**`)
- Password encryption using BCrypt (`JwtConfig.passwordEncoder`)

##### Multi-Tenant College Onboarding
- `College` entity captures domain/contact details; every `User`, `Student`, `Trainer`, and `Admin` links to a college
- Admin signup (`POST /api/v1/auth/admin/register`) creates a college and enforces a single admin per campus
- Student/Trainer signup requires selecting an existing college and capturing extra profile data (phone, department, register/teacher IDs)

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

#### 2.1 Authentication & Onboarding APIs
```
POST /api/v1/auth/login             # Issues cookies + returns profile
POST /api/v1/auth/logout            # Clears cookies + refresh token
POST /api/v1/auth/refresh           # Rotates refresh token, re-issues access cookie
GET  /api/v1/auth/me                # Returns current user profile (uses cookies)

POST /api/v1/auth/admin/register    # Creates college + admin (one per college)
POST /api/v1/auth/student/register  # Student signup (requires college + register number)
POST /api/v1/auth/trainer/register  # Trainer signup (requires college + teacher ID)

GET  /api/v1/colleges               # Public list of colleges for signup forms
```

Payload requirements:
- **Admin registration**: `adminName`, `adminEmail`, `password`, `phoneNumber`, `roleTitle`, plus college metadata (`collegeName`, `collegeDomain`, optional `collegeWebsite`, `collegeContactEmail`, `collegeContactPhone`, `collegeAddress`).
- **Student registration**: `name`, `email`, `password`, `phoneNumber`, `department`, `registerNumber`, `year`, `collegeId`.
- **Trainer registration**: `name`, `email`, `password`, `phoneNumber`, `department`, `teacherId`, `specialization`, optional `bio`, `collegeId`.

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
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN','TRAINER','STUDENT')),
    college_id UUID REFERENCES colleges(id)
);
```

##### College
```sql
CREATE TABLE colleges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL,
    domain VARCHAR(255) UNIQUE NOT NULL,
    website_url TEXT,
    contact_email VARCHAR(255),
    contact_phone VARCHAR(50),
    address TEXT,
    created_at TIMESTAMPTZ DEFAULT now()
);
```

##### Student / Trainer profile highlights
- `students`: `phone_number`, unique `register_number`, `year`, `college_id`
- `trainers`: `department`, `phone_number`, unique `teacher_id`, `college_id`

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

#### 4.1 JWT & Cookie Configuration
```java
@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

@Component
public class TokenCookieService {
    // Builds SameSite/HttpOnly cookies for access + refresh tokens
}
```

#### 4.2 Security Filters
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/api/v1/colleges/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/trainers/**").hasRole("TRAINER")
                .requestMatchers("/api/v1/students/**").hasRole("STUDENT")
                .anyRequest().authenticated())
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, tokenCookieService),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
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