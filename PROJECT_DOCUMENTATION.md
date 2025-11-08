# SkillBridge — Project Documentation

Last updated: 2025-10-31

## Summary

SkillBridge is a full-stack, role-based training management platform that helps colleges and training organizations manage batches, trainers, students, companies, progress tracking and feedback. The project is implemented as a Spring Boot backend with a React frontend.

This document summarizes the architecture, technology stack, backend endpoints and structure, frontend structure, database choices, and how to run the application locally.

## Tech Stack

- Backend: Java 17, Spring Boot, Spring Data JPA, Spring Security
- Build: Gradle
- Database: PostgreSQL (runtime dependency) and H2 for tests (test dependency). Data seeder and JPA entities indicate relational DB usage.
- Frontend: React (Create React App), Axios, React Router, Tailwind CSS
- Authentication: JWT-style token usage appears expected (frontend sends Bearer token). Current `SecurityConfig` is permissive for development.

## Repo layout (important top-level folders)

- `frontend/` — React app (CRA). Key files: `package.json`, `src/` (components, pages, services, routes)
- `src/main/java/com/college/skillbridge/` — Spring Boot application code
  - `config/` — `SecurityConfig`, `DataSeeder`, `WebConfig` etc.
  - `controllers/` — REST controllers (e.g., `AuthController`, `BatchController`, `CompanyController`, `FeedbackController`, ...)
  - `models/`, `repositories/`, `services/` — domain, data access, business logic
- `src/main/resources/` — application properties and YAML

## High-level architecture

- Client (React SPA)
  - Routes and role-based layouts (Admin, Trainer, Student)
  - Uses `frontend/src/services/apiService.js` to talk to backend at base URL `http://localhost:8080/api/v1`
  - `authService.js` manages token storage and login/logout flows

- Backend (Spring Boot)
  - Controller layer: handles HTTP requests, maps to services
  - Service layer: business logic
  - Repository layer: Spring Data JPA repositories for persistence
  - Security: `SecurityConfig` provides a `PasswordEncoder` and is configured permissively for development (all endpoints permitted). Production should tighten rules and enable JWT filter.
  - Data seeding: `DataSeeder` seeds skills, trainers, companies, admin, syllabi, batches, and a test student on startup when DB is empty

## Backend — notable files and responsibilities

- `SkillbridgeApplication.java` — Spring Boot main class
- `config/SecurityConfig.java` — configures `SecurityFilterChain` and a `BCryptPasswordEncoder`. Currently disables CSRF and permits all requests (development-friendly)
- `config/DataSeeder.java` — seeds initial domain data (skills, trainers, companies, admin, syllabi, batches, students) when repositories are empty. Uses repositories for: `StudentRepository`, `SkillRepository`, `BatchRepository`, `SyllabusRepository`, `CompanyRepository`, `TrainerRepository`, `AdminRepository` and a `PasswordEncoder` to store encoded passwords.

## Backend — controllers and API conventions

- Base URL used by frontend: `http://localhost:8080/api/v1`
- Controllers in the project (observed in the repository structure) include:
  - `AuthController` — login/register and token issuing (expected)
  - `BatchController` — create/list/update batches
  - `CompanyController` — manage companies
  - `FeedbackController` — feedback endpoints
  - Other controllers for `Student`, `Trainer`, `Admin`, `Syllabus`, `Report` etc.

Note: I did not exhaustively open every controller file in this pass, but you can quickly inspect all endpoints by searching for `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping` in `src/main/java/com/college/skillbridge/controllers`.

Suggested conventions used by the frontend and backend:
- Base API: `/api/v1` (from `frontend/src/services/apiService.js`)
- Auth token header: `Authorization: Bearer <token>` (added by axios interceptor in `apiService.js`)

Common endpoint patterns likely present:
- `POST /api/v1/auth/login` — authenticate (returns token)
- `POST /api/v1/auth/register` — create new user
- `GET /api/v1/batches` — list batches
- `POST /api/v1/batches` — create batch (admin)
- `GET /api/v1/companies` — list companies
- `POST /api/v1/feedback` — submit feedback

Open question / mismatch to note: the top-level `README.md` mentions MongoDB, while the Gradle configuration uses Spring Data JPA and includes `runtimeOnly 'org.postgresql:postgresql'` and tests include `com.h2database:h2`. The code (entities, repositories, `DataSeeder`) uses JPA-style models and repositories. Conclusion: the actual codebase targets a relational database (Postgres/H2). The README's MongoDB note appears stale/outdated and should be corrected.

## Database and data models

- Primary runtime DB: PostgreSQL (Gradle runtime dependency)
- Tests: H2 in-memory DB (test dependency)
- Entities exist under `models/` (examples seen in seeder: `Student`, `Trainer`, `Company`, `Skill`, `Batch`, `Syllabus`, `Admin`)
- Relationships: typical training-management relationships (e.g., Batch -> Syllabus, Student -> Batch, Trainer -> Batch, Student has Skills). Review the `models` directory for exact JPA mappings (`@Entity`, `@ManyToOne`, `@OneToMany`, etc.)

Seeded data (from `DataSeeder`):
- Skills: Java, Python, JavaScript, React, Angular, Spring Boot, Node.js, SQL, MongoDB, AWS, Docker, DSA
- Trainers: John Smith, Emily Johnson (passwords encoded as `password`)
- Companies: TechCorp, WebSolutions
- Admin user: admin@skillbridge.com (password `admin123` encoded)
- Syllabi & Batches: DSA Batch 2025 (8 weeks), Full Stack Batch 2025 (12 weeks)
- Student: Test Student (student@test.com, password `password`)

This seeder helps for local development and testing; remove or conditionally enable it for production environments.

## Frontend — structure and important files

- `frontend/src/App.js` — root React component, mounts `AppRoutes`
- `frontend/src/routes/` — routing and protected route logic (`AppRoutes.js`, `ProtectedRoute.jsx`)
- `frontend/src/layouts/` — role-based layout components (`AdminLayout.jsx`, `StudentLayout.jsx`, `TrainerLayout.jsx`)
- `frontend/src/pages/` — pages per role (Admin, Student, Trainer) plus `Login.jsx`, `Register.jsx`
- `frontend/src/components/` — shared components such as `Navbar.jsx`, `Sidebar.jsx`
- `frontend/src/services/apiService.js` — Axios instance, sets base URL to `http://localhost:8080/api/v1`, request interceptor injects token from `authService`, response interceptor handles 401 by redirecting to login
- `frontend/src/services/authService.js` — (exists) manages token and login state; used by `apiService`

Routing & authentication
- The app uses React Router. `ProtectedRoute.jsx` wraps routes requiring authorization. Routes are split by roles (admin/trainer/student) using layouts found in `layouts/`.

How the frontend calls the API
- All API calls use `apiService` which wraps axios and returns `{ success: boolean, data/error }` shapes
- The request interceptor adds `Authorization: Bearer <token>` when `AuthService.getToken()` returns a value

## How to run locally (development)

### 1. Configure Supabase environment

Create a copy of `supabase.env.example` and populate it with the credentials from your Supabase project (shared pooler recommended when connecting from IPv4 networks):

```bash
cp supabase.env.example .env.supabase
```

Update the new file with the real password from the Supabase connection string. The sample URL included in the example file expands to:

```
jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require
```

> **Note:** The generated user looks like `postgres.xuqcieuvmxgyjdvomakr` and the password may contain `@`. When exporting a connection URI ensure the password portion is percent-encoded if necessary (e.g. `24SkillBridge%40`).

Export the environment variables before starting the application:

```bash
export $(grep -v '^#' .env.supabase | xargs)
```

> Keep `.env.supabase` out of source control and rotate the password and JWT secret if they were ever shared publicly.

### 2. Start the backend (from repo root)

```bash
# start the Spring Boot backend
./gradlew bootRun

# or build and run the jar
./gradlew bootJar
java -jar build/libs/skillbridge-0.0.1-SNAPSHOT.jar
```

Notes:
- The Spring configuration now reads Supabase-specific variables (`SUPABASE_JDBC_URL`, `SUPABASE_DB_USER`, `SUPABASE_DB_PASSWORD`, etc.). Defaults still fall back to `localhost` for standalone development.
- Ensure the Supabase project has the expected schema (run `psql -f skillbridge_backup.sql` against the pooler connection if starting from scratch).
- For quick local development with an ephemeral database you can override `SUPABASE_JDBC_URL` to point at a local Postgres instance or H2 profile.

### 3. Validate connectivity

```bash
# 3a. Check that the credentials work
psql "postgresql://${SUPABASE_DB_USER}:${SUPABASE_DB_PASSWORD}@${SUPABASE_DB_HOST}:${SUPABASE_DB_PORT}/${SUPABASE_DB_NAME}?sslmode=require" -c '\dt'

# 3b. Run the application smoke test suite
./gradlew test

# 3c. Hit the health endpoint once the app is running
curl -H "Authorization: Bearer <jwt>" http://localhost:8080/api/test
```

Frontend (from `frontend/`):

```bash
cd frontend
npm install
npm start
```

The React dev server will be available at `http://localhost:3000`. Override the backend URL by creating `frontend/.env.local` with:

```
REACT_APP_API_URL=http://localhost:8080/api/v1
```

The runtime will fall back to `http://localhost:8080/api/v1` if the environment variable is not defined (see `frontend/src/services/apiService.js`).

### 4. Automate Supabase schema & seed

When you need to prime a fresh Supabase database, point the environment variables at your project and run:

```bash
export $(grep -v '^#' .env.supabase | xargs)
./scripts/bootstrap_supabase_schema.sh
```

This launches the backend in headless mode with Hibernate schema update enabled. All tables are created, the `users` table is populated with seed accounts, and the process exits automatically once initialization completes.

## Tests

- Backend unit/integration tests: `./gradlew test` (Gradle uses JUnit + Mockito; H2 is configured for tests)
- Frontend tests: `cd frontend && npm test` (CRA test runner)

## Developer notes & next steps

- Fix README inconsistency (MongoDB vs PostgreSQL/JPA). The codebase uses JPA + Postgres; update top-level README accordingly.
- Harden security before production: replace `permitAll()` with proper route rules, add JWT auth filter (if using JWT), configure CORS and CSRF appropriately.
- Externalize configuration via profiles: `application-dev.yml`, `application-prod.yml` to support different datasources and seeder toggles.
- Add API documentation (Swagger/OpenAPI) for discoverability: include `springdoc-openapi` or `springfox` dependency and expose `/swagger-ui.html`.
- Create a small script or README section that shows expected endpoint examples (curl/postman) once controllers are confirmed.

## Where to look next in the codebase

- Backend controllers: `src/main/java/com/college/skillbridge/controllers/`
- Domain models: `src/main/java/com/college/skillbridge/models/`
- Repositories: `src/main/java/com/college/skillbridge/repositories/`
- Services: `src/main/java/com/college/skillbridge/services/`
- Frontend routes & auth: `frontend/src/routes`, `frontend/src/services/authService.js`, `frontend/src/services/apiService.js`

---

If you'd like, I can now:

1. Open and list all controller endpoints and produce an endpoint reference (method, path, request/response DTOs). (recommended next step)
2. Add a Swagger/OpenAPI config to the backend so the API is self-documented.
3. Fix the README mismatch (replace MongoDB mention with Postgres/JPA) and add a small `application-dev.properties` example with Postgres connection values.

Tell me which of these you'd like me to do next (or say "All of the above") and I'll update the repo with the chosen changes.
