# Supabase Environment Validation Checklist

Use this list whenever you bring up a new environment or rotate credentials.

## 1. Database
- [ ] `psql "<supabase-connection-uri>" -c '\dt'` returns expected tables (`students`, `batches`, `trainers`, …).
- [ ] Spot check seed data: `SELECT name, email FROM students LIMIT 5;`.
- [ ] Review `skillbridge_backup.sql` for expected counts and reconcile differences.

## 2. Backend
- [ ] Export Supabase/JWT settings: `export $(grep -v '^#' .env.supabase | xargs)`.
- [ ] `./gradlew bootRun` starts without datasource or migration errors.
- [ ] `curl http://localhost:8080/api/test` returns the health message.
- [ ] `curl -X POST http://localhost:8080/api/v1/auth/login` with a seeded user responds with HTTP 200.
- [ ] `./gradlew test` passes.

## 3. Frontend
- [ ] `frontend/.env.local` contains `REACT_APP_API_URL` pointing to the backend.
- [ ] `npm run build` completes successfully.
- [ ] Logging in through the UI hits the backend (no mock tokens stored).

## 4. Security & Secrets
- [ ] Supabase password and JWT secret stored only in secure locations (not in git).
- [ ] `APP_SECURITY_CORS_ALLOWED_ORIGINS` set for the deployment domain.
- [ ] Review Supabase roles/policies for least-privilege as needed.

## 5. Optional Production Checks
- [ ] Container image builds succeed (`docker build` for backend and frontend).
- [ ] Supabase CPU/connection dashboards show expected levels after smoke tests.
- [ ] Monitoring and logging endpoints `/actuator/health`, `/actuator/metrics` reachable if enabled.

