# SkillBridge Documentation Index

Welcome to the SkillBridge documentation. This directory contains comprehensive documentation covering all aspects of the SkillBridge training management system.

## Documentation Structure

### 📋 [Project Overview](./project-overview.md)
**Start here** for a high-level understanding of the project.

- Problem statement and use cases
- Target users and business value
- Key differentiators
- Success metrics

**Read this first** if you're new to the project.

---

### 🛠️ [Technology Stack Analysis](./tech-stack-analysis.md)
Detailed analysis of the technology choices and their relevance.

- Backend stack (Spring Boot, Java 17, PostgreSQL)
- Frontend stack (React, Tailwind CSS)
- Security and authentication
- Caching and performance
- Stack compatibility and scalability
- Recommendations for improvements

**Read this** to understand technology decisions and assess stack relevance.

---

### 🏗️ [System Architecture](./architecture.md)
Complete architectural documentation of the application.

- High-level architecture diagram
- Backend layers (Controller, Service, Repository, Model)
- Frontend architecture
- Design patterns used
- Multi-tenancy architecture
- Data flow examples
- Security architecture
- Scalability considerations

**Read this** to understand how the system is structured and how components interact.

---

### 💾 [Data Model](./data-model.md)
Comprehensive database schema documentation.

- Entity relationship diagrams
- All entities with fields and relationships
- Enumerations and their values
- Indexes and performance considerations
- Data integrity rules
- Data seeding information

**Read this** to understand the database structure and data relationships.

---

### ⚙️ [Features and Workflows](./features-and-workflows.md)
Detailed documentation of all features and user workflows.

- Core features (12 major features)
- Intelligent batch recommendation algorithm
- Progress tracking system
- Bi-directional feedback system
- Authentication and authorization flows
- User workflows (Student, Trainer, Admin)
- Feature interactions

**Read this** to understand what the system does and how users interact with it.

---

### 🎨 [Frontend Structure](./frontend-structure.md)
Complete frontend architecture and component documentation.

- Frontend architecture overview
- Component structure
- Routing patterns
- State management
- Styling architecture
- API communication patterns
- Authentication flow
- Testing structure

**Read this** to understand the frontend codebase structure and patterns.

---

### 📚 Additional Documentation

#### [API Documentation](./api-documentation.md)
REST API endpoint documentation (if exists).

#### [Developer Run Guide](./developer-run-guide.md)
Step-by-step guide to set up and run the application locally.

#### [Deployment Guide](./deployment-guide.md)
Instructions for deploying the application to production.

#### [Technical Documentation](./technical-documentation.md)
Additional technical details and implementation notes.

#### [Validation Checklist](./validation-checklist.md)
Checklist for validating the application.

---

## Quick Navigation by Role

### For Developers
1. Start with [Project Overview](./project-overview.md)
2. Review [Technology Stack](./tech-stack-analysis.md)
3. Study [Architecture](./architecture.md)
4. Understand [Data Model](./data-model.md)
5. Review [Frontend Structure](./frontend-structure.md)
6. Follow [Developer Run Guide](./developer-run-guide.md)

### For Product Managers / Business Analysts
1. Start with [Project Overview](./project-overview.md)
2. Review [Features and Workflows](./features-and-workflows.md)
3. Check [Technical Documentation](./technical-documentation.md) for constraints

### For QA / Testers
1. Review [Features and Workflows](./features-and-workflows.md)
2. Check [API Documentation](./api-documentation.md)
3. Use [Validation Checklist](./validation-checklist.md)

### For DevOps / System Administrators
1. Review [Architecture](./architecture.md)
2. Follow [Deployment Guide](./deployment-guide.md)
3. Check [Technology Stack](./tech-stack-analysis.md) for infrastructure requirements

---

## Key Concepts

### Multi-Tenancy
SkillBridge supports multiple colleges, each with isolated data. All user entities are linked to a `college_id` for data isolation.

### Intelligent Batch Recommendation
The system uses a weighted scoring algorithm to recommend optimal batches to students based on:
- Skill matching (+2 points per match)
- Syllabus overlap (+3 points per new topic)
- Company relevance (+5 points per relevant company)

### Bi-Directional Feedback
Both trainers and students can provide ratings and feedback to each other, creating a two-way communication channel.

### Progress Tracking
Granular topic-level progress tracking with statuses: PENDING, IN_PROGRESS, COMPLETED, NEEDS_IMPROVEMENT.

### Role-Based Access Control
Three roles with distinct permissions:
- **ADMIN**: Full system access
- **TRAINER**: Batch and student management
- **STUDENT**: Personal profile and batch access

---

## Technology Summary

### Backend
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 17
- **Database**: PostgreSQL (production), H2 (testing)
- **Security**: Spring Security with JWT
- **Build Tool**: Gradle

### Frontend
- **Framework**: React 19.1.0
- **Routing**: React Router DOM 7.5.1
- **Styling**: Tailwind CSS 3.4.17
- **HTTP Client**: Axios 1.8.4
- **Build Tool**: Create React App

### Infrastructure
- **Database**: PostgreSQL (Supabase recommended)
- **Authentication**: JWT with HTTP-only cookies
- **Caching**: Caffeine (in-memory)
- **Deployment**: JAR file (backend), Static files (frontend)

---

## Getting Started

1. **New to the project?**
   - Read [Project Overview](./project-overview.md)
   - Review [Features and Workflows](./features-and-workflows.md)

2. **Setting up development?**
   - Follow [Developer Run Guide](./developer-run-guide.md)
   - Review [Technology Stack](./tech-stack-analysis.md)

3. **Understanding the codebase?**
   - Study [Architecture](./architecture.md)
   - Review [Data Model](./data-model.md)
   - Check [Frontend Structure](./frontend-structure.md)

4. **Deploying to production?**
   - Follow [Deployment Guide](./deployment-guide.md)
   - Review [Architecture](./architecture.md) for scalability

---

## Documentation Maintenance

This documentation is maintained alongside the codebase. When making significant changes:

1. Update relevant documentation files
2. Keep architecture diagrams current
3. Update API documentation for endpoint changes
4. Review and update workflows for feature changes

---

## Questions or Issues?

If you find any issues with the documentation or need clarification:

1. Check the relevant documentation file
2. Review the codebase for implementation details
3. Consult the technical documentation
4. Reach out to the development team

---

**Last Updated**: 2025-01-XX  
**Documentation Version**: 1.0

