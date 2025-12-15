# SkillBridge - Frontend Structure Documentation

## Frontend Architecture Overview

The SkillBridge frontend is a **Single Page Application (SPA)** built with React, following a component-based architecture with role-based routing and layouts.

## Technology Stack

- **Framework**: React 19.1.0
- **Build Tool**: Create React App (CRA)
- **Routing**: React Router DOM 7.5.1
- **HTTP Client**: Axios 1.8.4
- **Styling**: Tailwind CSS 3.4.17
- **Icons**: React Icons 5.5.0
- **Testing**: Jest + React Testing Library, Cypress

## Project Structure

```
frontend/
├── public/                 # Static assets
│   ├── index.html
│   ├── favicon.ico
│   └── manifest.json
├── src/
│   ├── components/         # Reusable UI components
│   ├── layouts/           # Role-based layout components
│   ├── pages/             # Page components
│   ├── routes/            # Routing configuration
│   ├── services/          # API service layer
│   ├── App.js             # Root component
│   ├── App.css            # Global styles
│   ├── index.js           # Application entry point
│   └── index.css          # Base styles
├── cypress/               # E2E tests
├── package.json
├── tailwind.config.js
└── postcss.config.js
```

## Component Architecture

### 1. Root Component (`App.js`)

**Location**: `src/App.js`

**Responsibility**: 
- Root component that renders `AppRoutes`
- Global application setup

**Structure**:
```jsx
function App() {
  return (
    <div className="App">
      <AppRoutes />
    </div>
  );
}
```

---

### 2. Routing Layer (`routes/`)

#### AppRoutes (`routes/AppRoutes.jsx`)

**Responsibility**: 
- Define all application routes
- Configure route protection
- Set up role-based routing

**Route Structure**:
- **Public Routes**:
  - `/` → Home page
  - `/login` → Login page
  - `/register` → Student/Trainer registration
  - `/college/signup` → Admin/College registration

- **Protected Routes** (Role-based):
  - `/admin/*` → Admin routes (requires ADMIN role)
  - `/student/*` → Student routes (requires STUDENT role)
  - `/trainer/*` → Trainer routes (requires TRAINER role)

**Implementation Pattern**:
```jsx
<Route path="/admin/*" element={<ProtectedRoute requiredRole="ADMIN" />}>
  <Route element={<AdminLayout />}>
    <Route path="dashboard" element={<AdminDashboard />} />
    <Route path="students" element={<AdminStudents />} />
    // ... more admin routes
  </Route>
</Route>
```

#### ProtectedRoute (`routes/ProtectedRoute.jsx`)

**Responsibility**: 
- Authentication check
- Role-based authorization
- Redirect to login if unauthorized

**Flow**:
1. Check if user is authenticated
2. Call `authService.ensureSession()` to validate session
3. Check user role against `requiredRole` prop
4. Redirect to `/login` if unauthorized
5. Render child routes if authorized

---

### 3. Layout Components (`layouts/`)

Layout components provide consistent structure for each role.

#### AdminLayout (`layouts/AdminLayout.jsx`)

**Structure**:
- Sidebar with admin navigation
- Navbar with user info and logout
- Main content area with `<Outlet />` for nested routes

**Menu Items**:
- Dashboard
- Students
- Batches
- Trainers
- Companies
- Feedback

#### StudentLayout (`layouts/StudentLayout.jsx`)

**Structure**:
- Sidebar with student navigation
- Navbar
- Main content area

**Menu Items**:
- Dashboard
- Profile
- Batch
- Apply
- Companies
- Feedback

#### TrainerLayout (`layouts/TrainerLayout.jsx`)

**Structure**:
- Sidebar with trainer navigation
- Navbar
- Main content area

**Menu Items**:
- Dashboard
- Batches
- Students
- Resources
- Feedback

---

### 4. Page Components (`pages/`)

Pages are organized by role and functionality.

#### Public Pages

**Home** (`pages/Home.jsx`)
- Landing page
- Public information about SkillBridge
- Links to login/register

**Login** (`pages/Login.jsx`)
- User authentication form
- Email and password input
- Redirects based on role after login

**Register** (`pages/Register.jsx`)
- Student/Trainer registration form
- College selection dropdown
- Profile information input
- Skills and projects management
- Link to college signup

**AdminSignup** (`pages/AdminSignup.jsx`)
- College and admin registration
- College information form
- Admin profile form
- Creates college and admin account

#### Admin Pages (`pages/admin/`)

**AdminDashboard** (`pages/admin/AdminDashboard.jsx`)
- Overview metrics
- Quick stats (students, batches, trainers)
- Recent activity
- Charts/graphs

**AdminStudents** (`pages/admin/AdminStudents.jsx`)
- List of all students
- Search and filter
- View student details
- Add new student

**AdminBatches** (`pages/admin/AdminBatches.jsx`)
- List of all batches
- Create new batch
- Edit batch details
- Assign trainers and companies

**AdminTrainers** (`pages/admin/AdminTrainers.jsx`)
- List of all trainers
- Add new trainer
- View trainer details
- Assign to batches

**AdminCompanies** (`pages/admin/AdminCompanies.jsx`)
- List of all companies
- Add new company
- Map companies to batches
- View hiring processes

**FeedbackSummary** (`pages/admin/FeedbackSummary.jsx`)
- Aggregated feedback data
- Average ratings
- Feedback by batch
- Top-rated trainers/students

#### Student Pages (`pages/student/`)

**StudentDashboard** (`pages/student/StudentDashboard.jsx`)
- Personal stats (CGPA, year, department)
- Current batch information
- Skills overview
- **Batch Recommendations** (key feature)
- Quick actions

**StudentProfile** (`pages/student/StudentProfile.jsx`)
- View and edit profile
- Skills management
- Projects portfolio
- Links (GitHub, portfolio, resume)

**StudentBatch** (`pages/student/StudentBatch.jsx`)
- Current batch details
- Syllabus topics
- Progress tracking
- Trainer information

**StudentApply** (`pages/student/StudentApply.jsx`)
- View recommended batches
- Batch details and match scores
- Apply to batch
- View application status

**StudentCompanies** (`pages/student/StudentCompanies.jsx`)
- Companies associated with batch
- Company details
- Hiring processes
- Filter by domain

**TrainerFeedback** (`pages/student/TrainerFeedback.jsx`)
- Rate and review trainers
- View feedback history
- Submit new feedback

#### Trainer Pages (`pages/trainer/`)

**TrainerDashboard** (`pages/trainer/TrainerDashboard.jsx`)
- Assigned batches overview
- Student count
- Recent activity
- Quick stats

**TrainerBatches** (`pages/trainer/TrainerBatches.jsx`)
- List of assigned batches
- Batch status
- Enrolled students count
- Navigate to batch details

**TrainerBatchDetails** (`pages/trainer/TrainerBatchDetails.jsx`)
- Batch information
- Syllabus topics
- Enrolled students list
- Progress overview

**TrainerStudents** (`pages/trainer/TrainerStudents.jsx`)
- List of all students in batches
- Student profiles
- Progress summary
- Filter by batch

**StudentFeedback** (`pages/trainer/StudentFeedback.jsx`)
- Rate and provide feedback to students
- View feedback history
- Update student progress

**TrainerResources** (`pages/trainer/TrainerResources.jsx`)
- Batch resources
- Upload/download materials
- Resource management

---

### 5. Reusable Components (`components/`)

#### Navbar (`components/Navbar.jsx`)

**Responsibility**:
- Top navigation bar
- User profile display
- Logout functionality
- Role-based menu items

**Features**:
- User name and role display
- Dropdown menu
- Logout button
- Responsive design

#### Sidebar (`components/Sidebar.jsx`)

**Responsibility**:
- Left navigation sidebar
- Role-based menu items
- Active route highlighting
- Collapsible (future)

**Props**:
- `role`: User role (admin, student, trainer)
- `menuItems`: Array of menu items with title, path, icon

**Structure**:
```jsx
<Sidebar role="admin" menuItems={adminMenuItems} />
```

---

### 6. Service Layer (`services/`)

The service layer abstracts API communication and provides a clean interface for components.

#### apiService (`services/apiService.js`)

**Responsibility**:
- Base Axios instance configuration
- Request/response interceptors
- Error handling
- Cookie management

**Configuration**:
- Base URL: `http://localhost:8080/api/v1` (configurable via env)
- `withCredentials: true` for cookie handling
- Automatic 401 redirect to login

**Methods**:
- `get(endpoint, params)` - GET request
- `post(endpoint, data)` - POST request
- `put(endpoint, data)` - PUT request
- `delete(endpoint)` - DELETE request

**Response Format**:
```javascript
{
  success: boolean,
  data?: any,
  error?: string
}
```

#### authService (`services/authService.js`)

**Responsibility**:
- Authentication operations
- User session management
- Token refresh handling

**Methods**:
- `login(email, password)` - User login
- `logout()` - User logout
- `register(data)` - User registration
- `getCurrentUser()` - Get current user from localStorage
- `ensureSession()` - Validate and refresh session
- `adminSignup(data)` - Admin/college registration
- `studentSignup(data)` - Student registration
- `trainerSignup(data)` - Trainer registration

**Session Management**:
- User profile stored in `localStorage` (lightweight)
- Session validated via `/api/v1/auth/me` endpoint
- Automatic token refresh on expiration

#### Role-Specific Services

**batchService** (`services/batchService.js`)
- Batch-related API calls
- Get batches, create batch, assign trainer/company

**studentService** (`services/studentService.js`)
- Student-related operations
- Get recommendations, update profile

**trainerService** (`services/trainerService.js`)
- Trainer-related operations
- Get batches, update progress

**companyService** (`services/companyService.js`)
- Company management
- Get companies, map to batches

**collegeService** (`services/collegeService.js`)
- College operations
- Get college list for signup forms

---

## State Management

### Current Approach

**Pattern**: Component-level state + localStorage

**State Storage**:
- **Component State**: `useState` hook for local component data
- **User Profile**: `localStorage` for user information
- **Session Validation**: API calls to `/api/v1/auth/me`

**Example**:
```jsx
const [studentData, setStudentData] = useState(null);
const [recommendedBatches, setRecommendedBatches] = useState([]);

useEffect(() => {
  const user = AuthService.getCurrentUser();
  // Fetch data based on user
}, []);
```

### Future Considerations

For complex state management, consider:
- **Redux**: For global state management
- **Zustand**: Lightweight alternative to Redux
- **React Context**: For shared state across components

---

## Styling Architecture

### Tailwind CSS

**Configuration**: `tailwind.config.js`

**Approach**: Utility-first CSS framework

**Benefits**:
- Rapid development
- Consistent design system
- Small bundle size (with purging)
- Responsive design utilities

**Usage Pattern**:
```jsx
<div className="bg-white rounded-lg shadow p-5 flex items-center space-x-4">
  <h1 className="text-2xl font-bold text-gray-800">Title</h1>
</div>
```

### Custom Styles

**Global Styles**: `src/index.css`
- Base Tailwind directives
- Custom CSS variables (if needed)
- Global resets

**Component Styles**: `src/App.css`
- Application-specific styles
- Overrides if needed

---

## API Communication Pattern

### Request Flow

1. **Component** calls service method
2. **Service** uses `apiService` (Axios wrapper)
3. **Axios** sends request with cookies automatically
4. **Backend** validates token and processes request
5. **Response** handled by service
6. **Component** updates state based on response

### Example

```jsx
// Component
const fetchBatches = async () => {
  const result = await batchService.getAllBatches();
  if (result.success) {
    setBatches(result.data);
  } else {
    setError(result.error);
  }
};

// Service
const getAllBatches = async () => {
  return await ApiService.get('/batches');
};
```

### Error Handling

**Pattern**: Centralized error handling in `apiService`

**401 Unauthorized**:
- Automatically redirects to `/login`
- Clears localStorage
- Prevents infinite redirect loops

**Other Errors**:
- Returns error message in response
- Component handles error display

---

## Authentication Flow

### Login Flow

1. User enters credentials
2. `authService.login()` called
3. POST request to `/api/v1/auth/login`
4. Backend validates and returns tokens in cookies
5. User profile returned in response
6. Profile stored in `localStorage`
7. Redirect based on role:
   - ADMIN → `/admin/dashboard`
   - STUDENT → `/student/dashboard`
   - TRAINER → `/trainer/dashboard`

### Session Validation

1. `ProtectedRoute` checks authentication
2. Calls `authService.ensureSession()`
3. GET request to `/api/v1/auth/me`
4. Backend validates token from cookie
5. Returns user profile if valid
6. Updates `localStorage` if needed
7. Allows route access

### Logout Flow

1. User clicks logout
2. `authService.logout()` called
3. POST request to `/api/v1/auth/logout`
4. Backend invalidates refresh token
5. Clears cookies
6. Clears `localStorage`
7. Redirects to `/login`

---

## Routing Patterns

### Nested Routes

**Pattern**: Layout components with nested routes

```jsx
<Route path="/admin/*" element={<ProtectedRoute requiredRole="ADMIN" />}>
  <Route element={<AdminLayout />}>
    <Route path="dashboard" element={<AdminDashboard />} />
    <Route path="students" element={<AdminStudents />} />
  </Route>
</Route>
```

### Route Protection

**Pattern**: `ProtectedRoute` wrapper component

```jsx
<Route 
  path="/student/*" 
  element={<ProtectedRoute requiredRole="STUDENT" />}
>
  {/* Protected routes */}
</Route>
```

### Default Routes

**Pattern**: Index routes with redirects

```jsx
<Route index element={<Navigate to="/admin/dashboard" replace />} />
```

---

## Performance Optimizations

### Current Optimizations

1. **Code Splitting**: React Router lazy loading (future)
2. **Image Optimization**: Optimized images in `public/`
3. **CSS Purging**: Tailwind removes unused styles
4. **Caching**: Browser caching for static assets

### Future Optimizations

1. **React.lazy()**: Lazy load route components
2. **Memoization**: `React.memo()` for expensive components
3. **Virtual Scrolling**: For long lists
4. **Service Worker**: Offline support (PWA)

---

## Testing Structure

### Unit Tests

**Location**: `src/**/*.test.js`

**Framework**: Jest + React Testing Library

**Example**:
```jsx
import { render, screen } from '@testing-library/react';
import Login from './Login';

test('renders login form', () => {
  render(<Login />);
  expect(screen.getByText('Login')).toBeInTheDocument();
});
```

### E2E Tests

**Location**: `cypress/e2e/`

**Framework**: Cypress

**Example**: `login.cy.js`
- Tests complete login flow
- Validates redirects
- Checks authentication state

---

## Environment Configuration

### Environment Variables

**File**: `.env.local` (not in repo)

**Variables**:
- `REACT_APP_API_URL`: Backend API URL (default: `http://localhost:8080/api/v1`)

**Usage**:
```javascript
const BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';
```

---

## Build and Deployment

### Development

```bash
npm start
# Runs on http://localhost:3000
```

### Production Build

```bash
npm run build
# Creates optimized build in `build/` directory
```

### Deployment

**Static Hosting**: 
- Serve `build/` directory via Nginx, Apache, or CDN
- Configure reverse proxy for API calls
- Set environment variables in hosting platform

---

## Best Practices

### Component Design

1. **Single Responsibility**: Each component has one clear purpose
2. **Reusability**: Extract common patterns into reusable components
3. **Props Validation**: Use PropTypes or TypeScript (future)
4. **Error Boundaries**: Wrap routes in error boundaries

### Code Organization

1. **Feature-Based**: Group related components together
2. **Service Layer**: Abstract API calls from components
3. **Constants**: Extract magic strings/numbers to constants
4. **Hooks**: Extract reusable logic into custom hooks

### Performance

1. **Avoid Unnecessary Renders**: Use `React.memo()` where appropriate
2. **Lazy Loading**: Load routes and heavy components lazily
3. **Debouncing**: Debounce search and filter inputs
4. **Pagination**: Implement pagination for large lists

---

## Future Enhancements

1. **TypeScript Migration**: Add type safety
2. **State Management**: Consider Redux or Zustand
3. **PWA Support**: Add service worker for offline capability
4. **Internationalization**: Add i18n support
5. **Dark Mode**: Implement theme switching
6. **Accessibility**: Improve ARIA labels and keyboard navigation
7. **Performance Monitoring**: Add error tracking (Sentry)

