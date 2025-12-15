# SkillBridge - Data Model Documentation

## Database Overview

**Database Type**: PostgreSQL (Production), H2 (Testing)  
**ORM**: Spring Data JPA / Hibernate  
**Primary Key Strategy**: UUID for most entities, Long for User entity  
**Naming Convention**: Snake_case for database columns, camelCase for Java properties

## Entity Relationship Diagram (High-Level)

```
College (1) ──< (N) User
College (1) ──< (N) Student
College (1) ──< (N) Trainer
College (1) ──< (N) Admin

User (1) ──< (N) RefreshToken

Student (1) ──< (N) StudentSkill ──> (N) Skill
Student (1) ──< (N) StudentBatchHistory ──> (N) Batch
Student (1) ──< (N) TrainingProgress ──> (N) Batch
Student (1) ──< (N) TrainingProgress ──> (N) SyllabusTopic
Student (1) ──< (N) StudentFeedback ──> (N) Trainer
Student (1) ──< (N) StudentFeedback ──> (N) Batch
Student (1) ──< (N) Project
Student (1) ──< (N) StudentAssessment ──> (N) Assessment

Batch (1) ──> (1) Syllabus ──< (N) SyllabusTopic
Batch (1) ──< (N) BatchTrainer ──> (N) Trainer
Batch (1) ──< (N) BatchCompanyMapping ──> (N) Company
Batch (1) ──< (N) StudentBatchHistory ──> (N) Student
Batch (1) ──< (N) TrainingProgress
Batch (1) ──< (N) BatchResource

Company (1) ──< (N) CompanyHiringProcess
Company (1) ──< (N) BatchCompanyMapping ──> (N) Batch

Trainer (1) ──< (N) BatchTrainer ──> (N) Batch
Trainer (1) ──< (N) TrainerFeedback ──> (N) Student
Trainer (1) ──< (N) TrainerFeedback ──> (N) Batch
Trainer (1) ──< (N) TrainingProgress (updated_by)
```

## Core Entities

### 1. College (Multi-Tenant Root)

**Table**: `colleges`  
**Purpose**: Multi-tenant isolation - each college is a separate tenant

**Fields**:
- `id` (UUID, PK): Unique identifier
- `name` (String, unique, not null): College name
- `domain` (String, unique, not null): College domain identifier
- `website_url` (String): College website
- `contact_email` (String): Contact email
- `contact_phone` (String): Contact phone
- `address` (TEXT): Physical address

**Relationships**:
- One-to-Many: `User`, `Student`, `Trainer`, `Admin`

**Key Constraints**:
- Unique: `name`, `domain`

---

### 2. User (Authentication Entity)

**Table**: `users`  
**Purpose**: Central authentication entity for all user types

**Fields**:
- `id` (Long, PK, Auto-generated): Unique identifier
- `name` (String, not null): User's full name
- `email` (String, unique, not null): Login email
- `password` (String, not null): BCrypt hashed password
- `role` (Enum: ADMIN, TRAINER, STUDENT, not null): User role
- `college_id` (UUID, FK): Reference to College

**Relationships**:
- Many-to-One: `College`
- One-to-Many: `RefreshToken`

**Key Constraints**:
- Unique: `email`
- Foreign Key: `college_id` → `colleges.id`

---

### 3. Student

**Table**: `students`  
**Purpose**: Student profile and academic information

**Fields**:
- `id` (UUID, PK): Unique identifier
- `name` (String, not null): Student name
- `email` (String, unique, not null): Student email
- `password` (String, not null): BCrypt hashed password
- `year` (Integer): Academic year
- `department` (String): Department name
- `phone_number` (String): Contact phone
- `register_number` (String, unique): Student registration number
- `cgpa` (Float): Cumulative GPA
- `problem_solved_count` (Integer): Number of problems solved (e.g., LeetCode)
- `github_link` (String): GitHub profile URL
- `portfolio_link` (String): Portfolio website URL
- `resume_link` (String): Resume document URL
- `college_id` (UUID, FK): Reference to College

**Relationships**:
- Many-to-One: `College`
- One-to-Many: `StudentSkill` → `Skill`
- One-to-Many: `StudentBatchHistory` → `Batch`
- One-to-Many: `TrainingProgress`
- One-to-Many: `Project`
- One-to-Many: `StudentFeedback` → `Trainer`
- One-to-Many: `StudentAssessment` → `Assessment`

**Key Constraints**:
- Unique: `email`, `register_number`
- Foreign Key: `college_id` → `colleges.id`

---

### 4. Trainer

**Table**: `trainers`  
**Purpose**: Trainer profile and specialization

**Fields**:
- `id` (UUID, PK): Unique identifier
- `name` (String, not null): Trainer name
- `email` (String, unique, not null): Trainer email
- `password` (String): BCrypt hashed password
- `specialization` (String): Training specialization
- `department` (String): Department name
- `phone_number` (String): Contact phone
- `teacher_id` (String, unique): Teacher identification number
- `bio` (TEXT): Trainer biography
- `college_id` (UUID, FK): Reference to College

**Relationships**:
- Many-to-One: `College`
- Many-to-Many: `Batch` (via `BatchTrainer`)
- One-to-Many: `TrainerFeedback` → `Student`
- One-to-Many: `TrainingProgress` (as `updated_by`)

**Key Constraints**:
- Unique: `email`, `teacher_id`
- Foreign Key: `college_id` → `colleges.id`

---

### 5. Admin

**Table**: `admins`  
**Purpose**: College administrator profile

**Fields**:
- `id` (UUID, PK): Unique identifier
- `name` (String, not null): Admin name
- `email` (String, unique, not null): Admin email
- `password` (String, not null): BCrypt hashed password
- `phone_number` (String): Contact phone
- `role_title` (String): Admin role/title
- `college_id` (UUID, FK): Reference to College

**Relationships**:
- Many-to-One: `College`

**Key Constraints**:
- Unique: `email`
- Foreign Key: `college_id` → `colleges.id`

---

### 6. Batch

**Table**: `batches`  
**Purpose**: Training batch information

**Fields**:
- `id` (UUID, PK): Unique identifier
- `name` (String, not null): Batch name
- `description` (TEXT): Batch description
- `duration_weeks` (Integer): Training duration in weeks
- `status` (Enum: UPCOMING, OPEN_FOR_ENROLLMENT, ACTIVE, COMPLETED, CANCELLED): Batch status
- `syllabus_id` (UUID, FK): Reference to Syllabus

**Relationships**:
- One-to-One: `Syllabus`
- One-to-Many: `StudentBatchHistory` → `Student`
- Many-to-Many: `Trainer` (via `BatchTrainer`)
- Many-to-Many: `Company` (via `BatchCompanyMapping`)
- One-to-Many: `TrainingProgress`
- One-to-Many: `BatchResource`

**Key Constraints**:
- Foreign Key: `syllabus_id` → `syllabi.id`

---

### 7. Syllabus

**Table**: `syllabi`  
**Purpose**: Training syllabus definition

**Fields**:
- `id` (UUID, PK): Unique identifier
- `title` (String, not null): Syllabus title
- `description` (TEXT): Syllabus description

**Relationships**:
- One-to-One: `Batch`
- One-to-Many: `SyllabusTopic`

---

### 8. SyllabusTopic

**Table**: `syllabus_topics`  
**Purpose**: Individual topics within a syllabus

**Fields**:
- `id` (UUID, PK): Unique identifier
- `syllabus_id` (UUID, FK): Reference to Syllabus
- `name` (String, not null): Topic name
- `description` (TEXT): Topic description
- `order_index` (Integer): Display order
- `difficulty_level` (Enum): Difficulty level
- `estimated_hours` (Integer): Estimated completion time

**Relationships**:
- Many-to-One: `Syllabus`
- One-to-Many: `TrainingProgress`

**Key Constraints**:
- Foreign Key: `syllabus_id` → `syllabi.id`

---

### 9. Skill

**Table**: `skills`  
**Purpose**: Available skills/technologies

**Fields**:
- `id` (UUID, PK): Unique identifier
- `name` (String, unique, not null): Skill name (e.g., "Java", "React")

**Relationships**:
- Many-to-Many: `Student` (via `StudentSkill`)

**Key Constraints**:
- Unique: `name`

---

### 10. StudentSkill

**Table**: `student_skills`  
**Purpose**: Student skill proficiency mapping

**Fields**:
- `id` (UUID, PK): Unique identifier
- `student_id` (UUID, FK): Reference to Student
- `skill_id` (UUID, FK): Reference to Skill
- `level` (Enum: BEGINNER, INTERMEDIATE, ADVANCED, not null): Proficiency level

**Relationships**:
- Many-to-One: `Student`
- Many-to-One: `Skill`

**Key Constraints**:
- Foreign Key: `student_id` → `students.id`
- Foreign Key: `skill_id` → `skills.id`
- Unique: `(student_id, skill_id)` (implied)

---

### 11. Company

**Table**: `companies`  
**Purpose**: Company information and hiring details

**Fields**:
- `id` (UUID, PK): Unique identifier
- `name` (String, not null): Company name
- `domain` (String): Company domain/industry
- `hiring_type` (Enum: FULL_TIME, INTERNSHIP, BOTH): Hiring type
- `notes` (TEXT): Additional notes

**Relationships**:
- One-to-Many: `CompanyHiringProcess`
- Many-to-Many: `Batch` (via `BatchCompanyMapping`)

---

### 12. CompanyHiringProcess

**Table**: `company_hiring_processes`  
**Purpose**: Detailed hiring process steps

**Fields**:
- `id` (UUID, PK): Unique identifier
- `company_id` (UUID, FK): Reference to Company
- `process_name` (String): Process step name
- `description` (TEXT): Process description
- `order_index` (Integer): Process order

**Relationships**:
- Many-to-One: `Company`

**Key Constraints**:
- Foreign Key: `company_id` → `companies.id`

---

### 13. BatchCompanyMapping

**Table**: `batch_company_mapping`  
**Purpose**: Many-to-many relationship between batches and companies

**Fields**:
- `id` (UUID, PK): Unique identifier
- `batch_id` (UUID, FK): Reference to Batch
- `company_id` (UUID, FK): Reference to Company

**Relationships**:
- Many-to-One: `Batch`
- Many-to-One: `Company`

**Key Constraints**:
- Foreign Key: `batch_id` → `batches.id`
- Foreign Key: `company_id` → `companies.id`
- Unique: `(batch_id, company_id)` (implied)

---

### 14. BatchTrainer

**Table**: `batch_trainers`  
**Purpose**: Many-to-many relationship between batches and trainers

**Fields**:
- `id` (UUID, PK): Unique identifier
- `batch_id` (UUID, FK): Reference to Batch
- `trainer_id` (UUID, FK): Reference to Trainer
- `role_description` (String): Trainer's role in the batch

**Relationships**:
- Many-to-One: `Batch`
- Many-to-One: `Trainer`

**Key Constraints**:
- Foreign Key: `batch_id` → `batches.id`
- Foreign Key: `trainer_id` → `trainers.id`
- Unique: `(batch_id, trainer_id)` (implied)

---

### 15. StudentBatchHistory

**Table**: `student_batch_history`  
**Purpose**: Student enrollment history in batches

**Fields**:
- `id` (UUID, PK): Unique identifier
- `student_id` (UUID, FK): Reference to Student
- `batch_id` (UUID, FK): Reference to Batch
- `start_date` (Date): Enrollment start date
- `end_date` (Date): Enrollment end date (nullable)
- `status` (Enum: ACTIVE, COMPLETED, DROPPED): Enrollment status

**Relationships**:
- Many-to-One: `Student`
- Many-to-One: `Batch`

**Key Constraints**:
- Foreign Key: `student_id` → `students.id`
- Foreign Key: `batch_id` → `batches.id`

---

### 16. TrainingProgress

**Table**: `training_progress`  
**Purpose**: Student progress tracking for syllabus topics

**Fields**:
- `id` (UUID, PK): Unique identifier
- `student_id` (UUID, FK): Reference to Student
- `batch_id` (UUID, FK): Reference to Batch
- `syllabus_topic_id` (UUID, FK): Reference to SyllabusTopic
- `status` (Enum: PENDING, IN_PROGRESS, COMPLETED, NEEDS_IMPROVEMENT): Progress status
- `feedback` (TEXT): Trainer feedback
- `updated_at` (Timestamp): Last update timestamp
- `updated_by` (UUID, FK): Reference to Trainer (who updated)

**Relationships**:
- Many-to-One: `Student`
- Many-to-One: `Batch`
- Many-to-One: `SyllabusTopic`
- Many-to-One: `Trainer` (as `updated_by`)

**Key Constraints**:
- Foreign Key: `student_id` → `students.id`
- Foreign Key: `batch_id` → `batches.id`
- Foreign Key: `syllabus_topic_id` → `syllabus_topics.id`
- Foreign Key: `updated_by` → `trainers.id`
- Unique: `(student_id, batch_id, syllabus_topic_id)` (implied)

---

### 17. StudentFeedback

**Table**: `student_feedback`  
**Purpose**: Student feedback/ratings for trainers

**Fields**:
- `id` (UUID, PK): Unique identifier
- `student_id` (UUID, FK): Reference to Student
- `trainer_id` (UUID, FK): Reference to Trainer
- `batch_id` (UUID, FK): Reference to Batch
- `comment` (TEXT): Feedback comment
- `rating` (Integer, 1-5): Rating score
- `timestamp` (Timestamp): Feedback submission time

**Relationships**:
- Many-to-One: `Student`
- Many-to-One: `Trainer`
- Many-to-One: `Batch`

**Key Constraints**:
- Foreign Key: `student_id` → `students.id`
- Foreign Key: `trainer_id` → `trainers.id`
- Foreign Key: `batch_id` → `batches.id`

---

### 18. TrainerFeedback

**Table**: `trainer_feedback`  
**Purpose**: Trainer feedback/ratings for students

**Fields**:
- `id` (UUID, PK): Unique identifier
- `trainer_id` (UUID, FK): Reference to Trainer
- `student_id` (UUID, FK): Reference to Student
- `batch_id` (UUID, FK): Reference to Batch
- `comment` (TEXT): Feedback comment
- `rating` (Integer, 1-5): Rating score
- `timestamp` (Timestamp): Feedback submission time

**Relationships**:
- Many-to-One: `Trainer`
- Many-to-One: `Student`
- Many-to-One: `Batch`

**Key Constraints**:
- Foreign Key: `trainer_id` → `trainers.id`
- Foreign Key: `student_id` → `students.id`
- Foreign Key: `batch_id` → `batches.id`

---

### 19. RefreshToken

**Table**: `refresh_tokens`  
**Purpose**: JWT refresh token storage

**Fields**:
- `id` (UUID, PK): Unique identifier
- `user_id` (Long, FK): Reference to User
- `token` (String, unique): Refresh token value
- `expiry_date` (Timestamp): Token expiration time
- `created_at` (Timestamp): Token creation time

**Relationships**:
- Many-to-One: `User`

**Key Constraints**:
- Foreign Key: `user_id` → `users.id`
- Unique: `token`

---

### 20. Project

**Table**: `projects`  
**Purpose**: Student project portfolio

**Fields**:
- `id` (UUID, PK): Unique identifier
- `student_id` (UUID, FK): Reference to Student
- `title` (String): Project title
- `description` (TEXT): Project description
- `github_link` (String): GitHub repository URL
- `live_link` (String): Live demo URL
- `technologies` (String[]): Technologies used

**Relationships**:
- Many-to-One: `Student`

**Key Constraints**:
- Foreign Key: `student_id` → `students.id`

---

## Enumerations

### Role
- `ADMIN`: College administrator
- `TRAINER`: Training instructor
- `STUDENT`: Student participant

### BatchStatus
- `UPCOMING`: Batch scheduled but not yet open
- `OPEN_FOR_ENROLLMENT`: Accepting student applications
- `ACTIVE`: Currently running
- `COMPLETED`: Training completed
- `CANCELLED`: Batch cancelled

### ProgressStatus
- `PENDING`: Not started
- `IN_PROGRESS`: Currently working on
- `COMPLETED`: Successfully completed
- `NEEDS_IMPROVEMENT`: Requires additional work

### SkillLevel / ProficiencyLevel
- `BEGINNER`: Basic knowledge
- `INTERMEDIATE`: Moderate proficiency
- `ADVANCED`: Expert level

### HiringType
- `FULL_TIME`: Full-time positions
- `INTERNSHIP`: Internship positions
- `BOTH`: Both types available

## Indexes and Performance

### Recommended Indexes

1. **User Authentication**:
   - Index on `users.email` (unique, already indexed)
   - Index on `users.college_id`

2. **Student Queries**:
   - Index on `students.email` (unique)
   - Index on `students.register_number` (unique)
   - Index on `students.college_id`
   - Index on `student_skills.student_id`
   - Index on `student_skills.skill_id`

3. **Batch Queries**:
   - Index on `batches.status`
   - Index on `batches.syllabus_id`
   - Index on `student_batch_history.student_id`
   - Index on `student_batch_history.batch_id`
   - Index on `student_batch_history.status`

4. **Progress Tracking**:
   - Composite index on `training_progress(student_id, batch_id, syllabus_topic_id)`
   - Index on `training_progress.status`

5. **Feedback Queries**:
   - Index on `student_feedback.student_id`
   - Index on `student_feedback.trainer_id`
   - Index on `trainer_feedback.trainer_id`
   - Index on `trainer_feedback.student_id`

## Data Integrity Rules

1. **Multi-Tenancy**: All user-related entities must have a valid `college_id`
2. **Cascade Deletes**: 
   - Deleting a student cascades to `StudentSkill`, `StudentBatchHistory`, `TrainingProgress`
   - Deleting a batch cascades to `StudentBatchHistory`, `TrainingProgress`, `BatchTrainer`, `BatchCompanyMapping`
3. **Unique Constraints**: Email addresses, registration numbers, teacher IDs must be unique
4. **Foreign Key Constraints**: All foreign keys must reference valid parent records
5. **Enum Validation**: Status and level fields must use valid enum values

## Data Seeding

The `DataSeeder` component seeds initial data when the database is empty:

- **Skills**: Java, Python, JavaScript, React, Angular, Spring Boot, Node.js, SQL, MongoDB, AWS, Docker, DSA
- **Default College**: "SkillBridge University"
- **Default Admin**: admin@skillbridge.com (password: admin123)
- **Sample Trainers**: John Smith, Emily Johnson
- **Sample Companies**: TechCorp, WebSolutions
- **Sample Batches**: DSA Batch 2025, Full Stack Batch 2025
- **Sample Student**: Test Student (student@test.com, password: password)

