# SkillBridge - System Architecture

## Architecture Overview

SkillBridge follows a **layered, multi-tier architecture** with clear separation of concerns. The system is designed as a **full-stack application** with a React frontend communicating with a Spring Boot backend via RESTful APIs.

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Layer (Browser)                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   React SPA  │  │  Tailwind    │  │  React Router │     │
│  │  Components  │  │     CSS      │  │   Protected   │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
                          │
                          │ HTTPS/REST API
                          │ (withCredentials: true)
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                  API Gateway / Backend Layer                │
│  ┌──────────────────────────────────────────────────────┐   │
│  │         Spring Boot Application (Port 8080)          │   │
│  │  ┌──────────────┐  ┌──────────────┐                │   │
│  │  │ Controllers  │  │ Security     │                │   │
│  │  │  (REST API)  │  │  (JWT Filter)│                │   │
│  │  └──────────────┘  └──────────────┘                │   │
│  │         │                  │                         │   │
│  │         ▼                  ▼                         │   │
│  │  ┌──────────────┐  ┌──────────────┐                │   │
│  │  │   Services   │  │   Aspects    │                │   │
│  │  │ (Business    │  │  (Caching)   │                │   │
│  │  │   Logic)     │  │              │                │   │
│  │  └──────────────┘  └──────────────┘                │   │
│  │         │                                            │   │
│  │         ▼                                            │   │
│  │  ┌──────────────┐                                   │   │
│  │  │ Repositories │                                   │   │
│  │  │  (Data Access)│                                  │   │
│  │  └──────────────┘                                   │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                          │
                          │ JPA/Hibernate
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                    Data Layer                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  PostgreSQL  │  │  Caffeine    │  │   H2 (Test)  │     │
│  │  (Production)│  │    Cache     │  │             │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

## Backend Architecture Layers

### 1. Controller Layer (`controllers/`)

**Responsibility**: Handle HTTP requests, validate input, and return responses.

**Key Controllers**:
- `AuthController`: Authentication and registration endpoints
- `BatchController`: Batch management operations
- `StudentController`: Student-related operations (recommendations, progress)
- `TrainerController`: Trainer management
- `CompanyController`: Company management
- `FeedbackController`: Feedback operations
- `CollegeController`: College management

**Pattern**: RESTful API design
- Base URL: `/api/v1`
- HTTP methods: GET, POST, PUT, DELETE
- Response format: JSON
- Status codes: 200, 201, 400, 401, 403, 404, 500

**Example Structure**:
```java
@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {
    @Autowired
    private BatchService batchService;
    
    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }
}
```

### 2. Service Layer (`services/`, `service/`)

**Responsibility**: Business logic, orchestration, and transaction management.

**Key Services**:
- `AuthService`: Authentication, token management, user registration
- `BatchService`: Batch operations, trainer/company assignments
- `StudentService`: Student management, batch recommendations
- `TrainerService`: Trainer operations
- `CompanyService`: Company management
- `FeedbackService`: Feedback operations (bi-directional)
- `ProgressTrackingService`: Student progress tracking

**Pattern**: Interface-based design with implementations
- Interfaces define contracts
- Implementations contain business logic
- Transaction management via `@Transactional`

**Example Structure**:
```java
@Service
public class BatchServiceImpl implements BatchService {
    @Autowired
    private BatchRepository batchRepository;
    
    @Override
    @Transactional
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }
}
```

### 3. Repository Layer (`repositories/`)

**Responsibility**: Data access and persistence operations.

**Pattern**: Spring Data JPA repositories
- Extends `JpaRepository<T, ID>`
- Automatic query generation from method names
- Custom queries with `@Query` annotation
- Pagination and sorting support

**Example Structure**:
```java
@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> {
    List<Batch> findByStatusIn(List<BatchStatus> statuses);
    Optional<Batch> findByName(String name);
}
```

### 4. Model/Entity Layer (`models/`)

**Responsibility**: Domain entities representing database tables.

**Pattern**: JPA entities with Lombok
- `@Entity` annotation for persistence
- `@Table` for custom table names
- Relationship mappings: `@OneToMany`, `@ManyToOne`, `@ManyToMany`
- UUID primary keys for most entities

**Key Entities**:
- `User`, `Student`, `Trainer`, `Admin`
- `Batch`, `Syllabus`, `SyllabusTopic`
- `Company`, `CompanyHiringProcess`
- `TrainingProgress`, `StudentFeedback`, `TrainerFeedback`
- `College` (multi-tenant support)

### 5. Security Layer (`security/`)

**Responsibility**: Authentication, authorization, and token management.

**Components**:
- `SecurityConfig`: Spring Security configuration
- `JwtTokenProvider`: JWT token generation and validation
- `JwtTokenFilter`: Request filtering for JWT validation
- `TokenCookieService`: Cookie management for tokens
- `CustomUserDetailsService`: User details loading
- `LoginAttemptService`: Brute-force protection

**Authentication Flow**:
1. User submits credentials via `/api/v1/auth/login`
2. Backend validates credentials
3. JWT tokens generated (access + refresh)
4. Tokens stored in HTTP-only cookies
5. Subsequent requests include cookies automatically
6. `JwtTokenFilter` validates token on each request
7. User context loaded into Spring Security context

**Authorization**: Role-based access control (RBAC)
- Roles: `ADMIN`, `TRAINER`, `STUDENT`
- Endpoints protected by role: `/api/v1/admin/**`, `/api/v1/trainers/**`, `/api/v1/students/**`

### 6. Configuration Layer (`config/`)

**Responsibility**: Application configuration and setup.

**Key Configurations**:
- `SecurityConfig`: Security and authentication setup
- `CorsConfig`: Cross-origin resource sharing
- `CacheConfig`: Caching configuration (Caffeine)
- `JwtConfig`: JWT token configuration
- `WebConfig`: Web-related configuration
- `DataSeeder`: Initial data seeding
- `MetricsConfig`: Application metrics

### 7. Cross-Cutting Concerns

#### Caching (`annotation/`, `aspect/`)
- **Custom Annotations**: `@CacheableWithTTL`, `@SlowApiCache`, `@UserSpecificCache`
- **Aspect**: `CachingAspect` - AOP-based caching implementation
- **Cache Provider**: Caffeine (in-memory cache)

#### Exception Handling (`exception/`)
- `GlobalExceptionHandler`: Centralized exception handling
- `BusinessException`: Custom business exceptions
- `ResourceNotFoundException`: Resource not found exceptions
- `ErrorResponse`: Standardized error response format

#### Validation (`validation/`)
- `PasswordValidator`: Password strength validation
- Input sanitization utilities

## Frontend Architecture

### 1. Component Structure

```
frontend/src/
├── components/          # Reusable UI components
│   ├── Navbar.jsx
│   └── Sidebar.jsx
├── layouts/            # Role-based layouts
│   ├── AdminLayout.jsx
│   ├── StudentLayout.jsx
│   └── TrainerLayout.jsx
├── pages/              # Page components
│   ├── admin/          # Admin pages
│   ├── student/        # Student pages
│   ├── trainer/        # Trainer pages
│   ├── Login.jsx
│   ├── Register.jsx
│   └── AdminSignup.jsx
├── routes/             # Routing configuration
│   ├── AppRoutes.jsx
│   └── ProtectedRoute.jsx
└── services/           # API services
    ├── apiService.js
    ├── authService.js
    ├── batchService.js
    └── ...
```

### 2. Routing Architecture

**Pattern**: React Router with protected routes

**Route Structure**:
- Public routes: `/`, `/login`, `/register`, `/college/signup`
- Protected routes: `/admin/*`, `/student/*`, `/trainer/*`
- Role-based access control via `ProtectedRoute` component

**Protected Route Flow**:
1. `ProtectedRoute` checks authentication status
2. Calls `authService.ensureSession()` to validate session
3. Checks user role against required role
4. Redirects to login if unauthorized
5. Renders child routes if authorized

### 3. State Management

**Current Approach**: Component-level state + Context (implicit)

**Pattern**:
- Local component state with `useState`
- Props drilling for shared state
- `localStorage` for user profile caching
- Session validation via API calls

**Future Consideration**: Consider Redux or Zustand for complex state management

### 4. API Communication

**Pattern**: Service layer with Axios

**Structure**:
- `apiService.js`: Base Axios instance with interceptors
- Role-specific services: `authService.js`, `batchService.js`, etc.
- Automatic cookie handling with `withCredentials: true`
- Error handling and 401 redirects

**Request Flow**:
1. Component calls service method
2. Service uses `apiService` (Axios wrapper)
3. Request includes cookies automatically
4. Response handled with success/error pattern
5. Component updates state based on response

## Design Patterns Used

### 1. Repository Pattern
- **Location**: `repositories/`
- **Purpose**: Abstraction of data access layer
- **Benefits**: Testability, maintainability, flexibility

### 2. Service Layer Pattern
- **Location**: `services/`, `service/`
- **Purpose**: Business logic encapsulation
- **Benefits**: Separation of concerns, reusability

### 3. DTO Pattern
- **Location**: `dto/`, `dtos/`
- **Purpose**: Data transfer objects for API communication
- **Benefits**: Decoupling, versioning, security

### 4. Factory Pattern
- **Location**: `config/DataSeeder`
- **Purpose**: Object creation for seeding
- **Benefits**: Centralized object creation

### 5. Strategy Pattern
- **Location**: Batch recommendation algorithm
- **Purpose**: Different scoring strategies for batch matching
- **Benefits**: Extensibility, maintainability

### 6. Aspect-Oriented Programming (AOP)
- **Location**: `aspect/CachingAspect`
- **Purpose**: Cross-cutting concerns (caching)
- **Benefits**: Separation of concerns, code reusability

### 7. Filter Pattern
- **Location**: `security/JwtTokenFilter`
- **Purpose**: Request filtering for authentication
- **Benefits**: Centralized authentication logic

## Multi-Tenancy Architecture

### College-Based Isolation

**Pattern**: Shared database with tenant isolation via `college_id`

**Implementation**:
- All user entities (`User`, `Student`, `Trainer`, `Admin`) have `college_id` foreign key
- Data filtered by college in queries
- College registration creates isolated tenant space

**Benefits**:
- Cost-effective (single database)
- Easy to manage
- Sufficient for current scale

**Considerations**:
- Ensure all queries filter by college
- Admin can only access their college's data
- Future: Consider row-level security (RLS) in PostgreSQL

## Data Flow Examples

### 1. Student Batch Recommendation Flow

```
Student Dashboard
    │
    ▼
StudentController.getRecommendedBatches()
    │
    ▼
StudentService.getRecommendedBatches()
    │
    ├─► Fetch student with skills
    ├─► Fetch available batches
    ├─► Calculate match scores:
    │   ├─► Skill match: +2 per match
    │   ├─► Syllabus overlap: +3 per topic
    │   └─► Company relevance: +5 per company
    │
    ▼
Sort by total score, return top 5
    │
    ▼
Return BatchRecommendationDto[]
```

### 2. Authentication Flow

```
Login Request
    │
    ▼
AuthController.login()
    │
    ▼
AuthService.authenticate()
    │
    ├─► Validate credentials
    ├─► Generate JWT tokens
    ├─► Store refresh token in DB
    └─► Set cookies (access + refresh)
    │
    ▼
Return AuthResponse with user profile
```

### 3. Progress Update Flow

```
Trainer updates progress
    │
    ▼
StudentController.updateProgress()
    │
    ▼
ProgressTrackingService.updateProgress()
    │
    ├─► Validate trainer-batch-student relationship
    ├─► Create/update TrainingProgress
    └─► Update timestamp and updated_by
    │
    ▼
Return updated progress
```

## Scalability Considerations

### Current Architecture Supports

1. **Horizontal Scaling**: Stateless JWT authentication allows multiple backend instances
2. **Database Connection Pooling**: HikariCP manages connections efficiently
3. **Caching**: Reduces database load for frequently accessed data
4. **Multi-Tenancy**: College-based isolation supports multiple institutions

### Future Enhancements

1. **Load Balancing**: Add reverse proxy (Nginx) for multiple backend instances
2. **Distributed Caching**: Migrate to Redis for shared cache across instances
3. **Message Queue**: Add async processing for heavy operations
4. **CDN**: Serve static assets from CDN
5. **Database Read Replicas**: Separate read/write operations for scale

## Security Architecture

### Defense in Depth

1. **Network Layer**: HTTPS (production)
2. **Application Layer**: 
   - JWT token validation
   - Role-based access control
   - Input sanitization
   - SQL injection prevention (JPA)
3. **Data Layer**: 
   - Password encryption (BCrypt)
   - HTTP-only cookies
   - CORS restrictions

### Security Components

- **Authentication**: JWT-based with refresh tokens
- **Authorization**: Role-based (ADMIN, TRAINER, STUDENT)
- **Session Management**: Stateless (JWT) with refresh token rotation
- **Password Security**: BCrypt hashing with salt
- **Cookie Security**: HTTP-only, Secure flag, SameSite

## Deployment Architecture

### Development
- Backend: `./gradlew bootRun` (port 8080)
- Frontend: `npm start` (port 3000)
- Database: PostgreSQL (local or Supabase)

### Production (Recommended)
- Backend: JAR file on application server
- Frontend: Static files served via Nginx/CDN
- Database: Managed PostgreSQL (Supabase/AWS RDS)
- Reverse Proxy: Nginx for load balancing and SSL termination

