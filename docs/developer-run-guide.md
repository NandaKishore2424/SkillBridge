# SkillBridge Developer Run & Git Push Guide

This runbook collects the essential steps for getting SkillBridge running locally and pushing your work to GitHub. Follow it from top to bottom when you are setting up a new machine or preparing to ship changes.

---

## 1. Prerequisites

- **Java 17** (use `sdkman`, `asdf`, or your package manager)
- **Node.js 18+ and npm** (ships with recent Node releases)
- **PostgreSQL client tools** (for `psql`; install via `sudo apt install postgresql-client-common`)
- **Supabase project** credentials (database URL, user, password, JWT secret)
- **Git** with access to your GitHub account (SSH key or token)

> Tip: Export your Supabase credentials once and store them in `.env.supabase` (already git-ignored).

---

## 2. Configure Environment Variables

1. Copy the example template:
   ```bash
   cp supabase.env.example .env.supabase
   ```
2. Edit `.env.supabase` and populate:
   - `SUPABASE_DB_HOST`, `SUPABASE_DB_PORT`, `SUPABASE_DB_NAME`
   - `SUPABASE_DB_USER`, `SUPABASE_DB_PASSWORD`
   - `SUPABASE_JDBC_URL` (use the IPv4 session pooler URL from Supabase)
   - `JWT_SECRET` (32-byte secret generated with `openssl rand -base64 32`)
3. Load the file when starting the backend:
   ```bash
   export $(grep -v '^#' .env.supabase | xargs)
   ```

> Ensure `supabase.env.example` remains untouched so it continues guiding future contributors.

---

## 3. Bootstrap the Database Schema

Run the Spring Boot schema bootstrapper once per fresh Supabase instance (or whenever you want to reset the seed data):

```bash
chmod +x scripts/bootstrap_supabase_schema.sh
./scripts/bootstrap_supabase_schema.sh
```

This command:
- Builds the backend
- Runs Hibernate DDL against Supabase
- Seeds core data (admin, trainers, student, skills, companies, batches)
- Exits automatically once seeding completes

You can verify the data via the Supabase SQL Editor or `psql`.

---

## 4. Start the Backend (Spring Boot)

From the repository root:

```bash
cd /home/nanda-kishore-r/SkillBridge
export $(grep -v '^#' .env.supabase | xargs)
./gradlew bootRun
```

Key notes:
- Leave this terminal running; it hosts the API on `http://localhost:8080`.
- Health check: `curl http://localhost:8080/api/test`
- Default seeded logins:
  - Admin – `admin@skillbridge.com` / `admin123`
  - Trainer – `john.smith@example.com` / `password`
  - Trainer – `emily.johnson@example.com` / `password`
  - Student – `student@test.com` / `password`

---

## 5. Start the Frontend (React)

Open a second terminal:

```bash
cd /home/nanda-kishore-r/SkillBridge/frontend
npm install        # first-time only
npm start
```

The React dev server runs on `http://localhost:3000` (or the next free port). It reads `REACT_APP_API_URL` from `frontend/.env.supabase`. Refresh the browser after restarting the backend to avoid stale API tokens.

---

## 6. Optional Checks

- **Run backend unit tests**: `./gradlew test`
- **Run frontend tests**: `npm test`
- **Lint frontend**: `npm run lint`
- **Inspect database**: `psql -h <session-pooler-host> -U <user> -d postgres`

---

## 7. Git & GitHub Workflow

1. **Check status**
   ```bash
   git status
   ```
2. **Stage files**
   ```bash
   git add <file1> <file2>
   # or stage everything
   git add .
   ```
3. **Commit with a clear message**
   ```bash
   git commit -m "feat: describe the change clearly"
   ```
4. **Set the remote (first time only)**
   ```bash
   git remote add origin git@github.com:<username>/<repo>.git
   # or for HTTPS:
   git remote add origin https://github.com/<username>/<repo>.git
   ```
5. **Push your branch**
   ```bash
   git push -u origin <branch-name>
   ```
6. **Open a Pull Request**
   - Go to the GitHub repository
   - Click **Compare & pull request**
   - Fill in title + description summarising backend, frontend, schema changes

> Keep your branch in sync with `main` using `git pull --rebase origin main`. Resolve conflicts locally, rerun tests, and push again.

---

## 8. Common Troubleshooting

| Issue | Resolution |
|-------|------------|
| `Web server failed to start. Port 8080 was already in use.` | Find & kill the process: `lsof -t -i :8080` then `kill <pid>` |
| `Driver org.postgresql.Driver claims to not accept jdbcUrl ${SUPABASE_JDBC_URL}` | Ensure `.env.supabase` is exported in the current shell before `bootRun`. |
| `psql: command not found` | Install PostgreSQL client: `sudo apt install postgresql-client-common`. |
| Frontend API calls fail | Confirm backend is running, `REACT_APP_API_URL` matches it, and JWT tokens are valid. |

---

## 9. Deployment Snapshot

- Backend: Spring Boot 3.4, Gradle build -> Supabase PostgreSQL via JDBC
- Frontend: React + Tailwind, hitting `REACT_APP_API_URL`
- Schema Automation: `scripts/bootstrap_supabase_schema.sh`
- Secrets: `.env.supabase` (backend) + `frontend/.env.supabase` (frontend)

Refer to `docs/deployment-guide.md` for container-based or production deployments.

---

By keeping this document updated, we ensure every contributor can spin up SkillBridge quickly and push their changes without guesswork. If your workflow differs (Docker, CI pipelines, etc.), add a short section so the guide stays comprehensive.

