# SkillBridge - Features and Workflows

## Core Features

### 1. Multi-Tenant College Management

**Description**: Support for multiple colleges/institutions, each with isolated data and users.

**Key Components**:
- College registration and onboarding
- College-based data isolation
- College admin management

**Workflow**:
1. College admin registers college via `/college/signup`
2. System creates college record with unique domain
3. Admin account created and linked to college
4. All subsequent users (students, trainers) linked to college
5. Data queries automatically filtered by `college_id`

---

### 2. Intelligent Batch Recommendation System

**Description**: AI-powered batch recommendation engine that matches students to optimal training batches based on skills, syllabus, and company alignment.

**Algorithm**:
The system uses a **weighted scoring algorithm** with three factors:

1. **Skill Match Score** (+2 points per match)
   - Compares student skills with batch technologies
   - Bonus points for higher proficiency levels:
     - Intermediate: +1 bonus
     - Advanced: +2 bonus

2. **Syllabus Overlap Score** (+3 points per new topic)
   - Identifies new topics student will learn
   - Excludes topics already covered in previous batches
   - Rewards learning opportunities

3. **Company Relevance Score** (+5 points per relevant company)
   - Matches student skills with company domains
   - Bonus (+2) if company is actively hiring
   - Considers hiring type (Full-time, Internship, Both)

**Total Score Calculation**:
```
Total Score = Skill Match Score + Syllabus Overlap Score + Company Relevance Score
```

**Workflow**:
1. Student views dashboard
2. System fetches student profile with skills
3. System retrieves all active batches
4. For each batch:
   - Extract technologies from syllabus topics
   - Match student skills against technologies
   - Calculate new learning opportunities
   - Check company alignments
   - Compute weighted score
5. Sort batches by total score (descending)
6. Return top 5 recommendations with match reasons
7. Display recommendations with:
   - Batch name and description
   - Match score breakdown
   - Match reasons (e.g., "3 of your skills match", "You'll learn 5 new topics")
   - Trainer information
   - Associated companies

**Example Match Reasons**:
- "3 of your skills match this batch's technologies"
- "You'll learn 5 new topics including React Hooks, Node.js APIs"
- "2 companies aligned with your skills are associated with this batch"
- "1 company is currently hiring for similar roles"

---

### 3. Student Management

**Description**: Comprehensive student profile management with skills, projects, and academic information.

**Key Features**:
- Student registration with profile
- Skill management (add, update proficiency levels)
- Project portfolio management
- Academic information (CGPA, year, department)
- Problem-solving metrics (LeetCode, etc.)
- Links to GitHub, portfolio, resume

**Workflow - Student Registration**:
1. Student navigates to `/register`
2. Selects college from dropdown
3. Fills registration form:
   - Personal info (name, email, password)
   - Academic info (year, department, register number, CGPA)
   - Contact info (phone)
   - Skills (with proficiency levels)
   - Projects (optional)
   - Links (GitHub, portfolio, resume)
4. System validates input
5. Password hashed with BCrypt
6. Student record created with `college_id`
7. Skills linked via `StudentSkill` junction table
8. Redirect to login

**Workflow - Profile Update**:
1. Student navigates to profile page
2. Views current profile information
3. Updates fields (skills, projects, links)
4. System validates and saves changes
5. Profile updated in database

---

### 4. Batch Management

**Description**: Complete batch lifecycle management from creation to completion.

**Batch Lifecycle**:
1. **UPCOMING**: Batch scheduled but not yet open
2. **OPEN_FOR_ENROLLMENT**: Accepting student applications
3. **ACTIVE**: Currently running training
4. **COMPLETED**: Training finished
5. **CANCELLED**: Batch cancelled

**Workflow - Batch Creation (Admin)**:
1. Admin navigates to "Add Batch"
2. Fills batch form:
   - Name, description
   - Duration (weeks)
   - Status
   - Syllabus selection/creation
3. System creates batch record
4. Admin assigns trainers (optional)
5. Admin maps companies (optional)
6. Batch available for student enrollment

**Workflow - Batch Assignment (Student)**:
1. Student views recommended batches
2. Student selects batch to apply
3. System creates `StudentBatchHistory` record
4. Status set to `ACTIVE`
5. Student enrolled in batch
6. Progress tracking initialized for all syllabus topics

---

### 5. Progress Tracking System

**Description**: Granular topic-level progress tracking with status management and trainer feedback.

**Progress Statuses**:
- **PENDING**: Topic not yet started
- **IN_PROGRESS**: Student currently working on topic
- **COMPLETED**: Topic successfully completed
- **NEEDS_IMPROVEMENT**: Requires additional work

**Workflow - Progress Update (Trainer)**:
1. Trainer navigates to batch details
2. Views list of enrolled students
3. Selects student to update progress
4. Views syllabus topics with current status
5. Updates topic status:
   - Selects status from dropdown
   - Adds feedback comment (optional)
6. System creates/updates `TrainingProgress` record
7. Timestamp and `updated_by` (trainer) recorded
8. Progress visible to student

**Workflow - Progress View (Student)**:
1. Student navigates to batch page
2. Views syllabus topics
3. Sees progress status for each topic:
   - Visual indicator (color-coded)
   - Status label
   - Trainer feedback (if available)
   - Last updated timestamp
4. Can filter by status

**Progress Calculation**:
- Overall batch progress = (Completed topics / Total topics) × 100
- Displayed as percentage on dashboard

---

### 6. Bi-Directional Feedback System

**Description**: Two-way rating and feedback system between trainers and students.

#### Trainer → Student Feedback

**Workflow**:
1. Trainer navigates to student feedback page
2. Selects batch and student
3. Provides feedback:
   - Rating (1-5 stars)
   - Comment (text)
4. System creates `TrainerFeedback` record
5. Feedback linked to batch, student, trainer
6. Timestamp recorded
7. Student can view feedback

**Use Cases**:
- Performance evaluation
- Skill assessment
- Improvement suggestions
- Recognition of achievements

#### Student → Trainer Feedback

**Workflow**:
1. Student navigates to trainer feedback page
2. Selects batch and trainer
3. Provides feedback:
   - Rating (1-5 stars)
   - Comment (text)
4. System creates `StudentFeedback` record
5. Feedback linked to batch, trainer, student
6. Timestamp recorded
7. Trainer can view feedback

**Use Cases**:
- Teaching effectiveness
- Communication quality
- Support and guidance
- Overall experience

#### Feedback Aggregation

**Admin Reports**:
- Average ratings per trainer
- Average ratings per student
- Top-rated trainers
- Feedback summary by batch
- Feedback trends over time

**Workflow - Feedback Summary (Admin)**:
1. Admin navigates to feedback summary
2. Selects batch (optional)
3. System aggregates:
   - Trainer ratings (average, count)
   - Student ratings (average, count)
   - Feedback comments
4. Displays summary with charts/graphs
5. Export capability (future)

---

### 7. Company Management

**Description**: Company information management with hiring process tracking and batch alignment.

**Workflow - Add Company (Admin)**:
1. Admin navigates to "Add Company"
2. Fills company form:
   - Name, domain
   - Hiring type (Full-time, Internship, Both)
   - Notes
3. System creates company record
4. Admin adds hiring process steps (optional):
   - Process name
   - Description
   - Order index
5. Company available for batch mapping

**Workflow - Map Company to Batch**:
1. Admin navigates to batch details
2. Clicks "Map Companies"
3. Selects companies from list
4. System creates `BatchCompanyMapping` records
5. Companies visible to students in batch
6. Used in batch recommendation algorithm

**Workflow - View Companies (Student)**:
1. Student navigates to companies page
2. Views companies associated with their batch
3. Filters by domain (optional)
4. Views company details:
   - Name, domain
   - Hiring type
   - Hiring process steps
   - Notes
5. Can view other batches' companies (read-only)

---

### 8. Trainer Management

**Description**: Trainer profile management and batch assignment.

**Workflow - Add Trainer (Admin)**:
1. Admin navigates to "Add Trainer"
2. Fills trainer form:
   - Personal info (name, email, password)
   - Specialization
   - Department
   - Phone, teacher ID
   - Bio
3. System creates trainer record
4. Trainer account linked to college
5. Trainer can login and access dashboard

**Workflow - Assign Trainer to Batch**:
1. Admin navigates to batch details
2. Clicks "Assign Trainer"
3. Selects trainer from list
4. Enters role description (e.g., "Main Instructor", "Mentor")
5. System creates `BatchTrainer` record
6. Trainer can view batch in their dashboard
7. Multiple trainers can be assigned to one batch

**Workflow - Trainer Dashboard**:
1. Trainer logs in
2. Views assigned batches
3. Sees enrolled students per batch
4. Accesses batch resources
5. Updates student progress
6. Provides feedback

---

### 9. Syllabus Management

**Description**: Structured syllabus creation with topics and learning paths.

**Workflow - Create Syllabus**:
1. Admin navigates to batch creation
2. Creates/selects syllabus:
   - Title, description
   - Topics (name, description, order, difficulty, estimated hours)
3. System creates syllabus with topics
4. Syllabus linked to batch
5. Topics used for progress tracking

**Syllabus Structure**:
- Syllabus (container)
  - Topic 1
    - Name, description
    - Difficulty level
    - Estimated hours
    - Order index
  - Topic 2
  - ...

**Workflow - View Syllabus (Student/Trainer)**:
1. User navigates to batch details
2. Views syllabus topics
3. Sees topic information:
   - Name, description
   - Difficulty level
   - Estimated hours
   - Progress status (for students)
4. Topics ordered by `order_index`

---

### 10. Authentication and Authorization

**Description**: Secure JWT-based authentication with role-based access control.

**Authentication Flow**:
1. User submits credentials via `/api/v1/auth/login`
2. System validates credentials
3. JWT tokens generated:
   - Access token (short-lived, ~24 hours)
   - Refresh token (long-lived, ~7 days)
4. Tokens stored in HTTP-only cookies
5. User profile returned in response
6. Frontend stores user profile in localStorage (lightweight)

**Authorization Flow**:
1. User makes authenticated request
2. `JwtTokenFilter` intercepts request
3. Extracts token from cookie
4. Validates token signature and expiration
5. Loads user details into Spring Security context
6. Checks role against endpoint requirements
7. Allows/denies access based on role

**Roles and Permissions**:
- **ADMIN**: Full access to `/api/v1/admin/**`
  - Manage students, trainers, batches, companies
  - View reports and feedback summaries
- **TRAINER**: Access to `/api/v1/trainers/**`
  - View assigned batches
  - Update student progress
  - Provide feedback
- **STUDENT**: Access to `/api/v1/students/**`
  - View profile and batches
  - View recommendations
  - Submit feedback
  - Track progress

**Refresh Token Flow**:
1. Access token expires
2. Frontend calls `/api/v1/auth/refresh`
3. System validates refresh token
4. New access token generated
5. New token set in cookie
6. Request continues

---

### 11. Caching System

**Description**: Performance optimization through intelligent caching.

**Cache Types**:
1. **Slow API Cache**: Caches slow database queries
   - TTL: Configurable
   - Scope: Method-level
2. **User-Specific Cache**: Caches user-specific data
   - TTL: Configurable
   - Scope: Per user
3. **TTL-Based Cache**: General-purpose caching
   - TTL: Configurable per annotation
   - Scope: Method-level

**Implementation**:
- Custom annotations: `@CacheableWithTTL`, `@SlowApiCache`, `@UserSpecificCache`
- AOP aspect: `CachingAspect` intercepts annotated methods
- Cache provider: Caffeine (in-memory)

**Workflow**:
1. Method annotated with caching annotation
2. AOP aspect intercepts method call
3. Checks cache for result
4. If cached and valid, returns cached result
5. If not cached or expired, executes method
6. Stores result in cache
7. Returns result

---

### 12. Reporting and Analytics

**Description**: Admin dashboard with key metrics and reports.

**Key Metrics**:
- Total students, trainers, batches
- Active batches count
- Average student ratings
- Average trainer ratings
- Batch completion rates
- Feedback summary

**Workflow - View Reports (Admin)**:
1. Admin navigates to dashboard
2. Views key metrics cards
3. Clicks on specific report
4. System aggregates data:
   - Student counts by batch
   - Progress statistics
   - Feedback summaries
5. Displays charts/graphs
6. Export capability (future)

---

## User Workflows Summary

### Student Journey
1. **Registration** → Profile creation with skills
2. **Login** → Access dashboard
3. **View Recommendations** → See personalized batch suggestions
4. **Apply to Batch** → Enroll in training batch
5. **Track Progress** → View topic-level progress
6. **Submit Feedback** → Rate and review trainers
7. **View Companies** → See hiring opportunities

### Trainer Journey
1. **Registration/Login** → Access dashboard
2. **View Batches** → See assigned batches
3. **View Students** → See enrolled students
4. **Update Progress** → Track student progress on topics
5. **Provide Feedback** → Rate and comment on students
6. **View Feedback** → See student feedback received

### Admin Journey
1. **College Registration** → Set up college account
2. **Login** → Access admin dashboard
3. **Manage Users** → Add students, trainers
4. **Create Batches** → Set up training programs
5. **Map Companies** → Link companies to batches
6. **Monitor Progress** → View overall training progress
7. **View Reports** → Analyze feedback and metrics

---

## Feature Interactions

### Batch Recommendation + Progress Tracking
- Recommendations consider student's previous batch history
- Progress data influences future recommendations

### Feedback + Progress Tracking
- Trainers provide feedback based on progress observations
- Feedback influences student motivation and performance

### Company Mapping + Batch Recommendation
- Companies mapped to batches influence recommendation scores
- Students see hiring opportunities aligned with their training

### Multi-Tenancy + All Features
- All features respect college boundaries
- Data isolation ensures privacy and security

