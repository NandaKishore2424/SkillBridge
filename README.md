# SkillBridge - Training Management System

A full-stack role-based platform for managing student training batches, skills, company hiring processes, trainer assignments, progress tracking, and bi-directional feedback.

## 🚀 Tech Stack

> Frontend: React.js, Tailwind CSS

> Backend: Spring Boot (Java), Gradle

> Database: MongoDB

## 📂 Project Structure

### Backend

> Spring Boot Application

> JPA Entity Models

> Services + Business Logic

> REST Controllers

> JWT-based Authentication (basic)

> Unit-tested API Endpoints

### Frontend

> React.js SPA

> Tailwind CSS for UI

> Role-Based Dashboards (Admin, Student, Trainer)

> Protected Routes

> API Service Integration

## ✨ Key Features

### 1. Role-Based Access

> Admin, Trainer, and Student dashboards

> Different permissions and views based on user role

### 2. Student Management
   
> Profile management (personal info, skills, projects)

> Intelligent batch assignments

> View assigned syllabus and companies

### 3. Batch Management

> Create and manage training batches

> Assign trainers and companies

> Syllabus management per batch

### 4. Trainer Management
   
> Manage assigned batches

> Track student progress

> Provide feedback on students

### 5. Company Hiring Management

> Add companies and their hiring processes

> Map companies to training batches

> Filter companies by domain

### 6. Batch Recommendation System

#### Logic:

> Match student's skills to syllabus

> Prioritize based on company hiring needs

> Weighted scoring system:

 1. Skill match: +2 points

 2. Syllabus match: +3 points

 3. Company relevance: +5 points

> Result:

   -> Smart, personalized batch suggestions shown to students

7. Progress Tracking
   
> Student-level topic progress tracking

> Progress statuses: Pending, In Progress, Completed, Needs Improvement

> Trainer can update and monitor batch progress

### 8. Bi-Directional Feedback System

> Trainers rate and provide feedback to students

> Students rate and review trainers

> 5-star rating system with history tracking

> Admin can view feedback summaries

