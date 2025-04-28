import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

// Auth and public pages
import Home from '../pages/Home';
import Login from '../pages/Login';
import Register from '../pages/Register';

// Layouts
import AdminLayout from '../layouts/AdminLayout';
import StudentLayout from '../layouts/StudentLayout';
import TrainerLayout from '../layouts/TrainerLayout';

// Admin pages
import AdminDashboard from '../pages/admin/AdminDashboard';
import AdminStudents from '../pages/admin/AdminStudents';
import AdminBatches from '../pages/admin/AdminBatches';
import AdminTrainers from '../pages/admin/AdminTrainers';
import AdminCompanies from '../pages/admin/AdminCompanies';
import FeedbackSummary from '../pages/admin/FeedbackSummary';
import BatchFeedbackDetails from '../pages/admin/BatchFeedbackDetails';

// Student pages
import StudentDashboard from '../pages/student/StudentDashboard';
import StudentProfile from '../pages/student/StudentProfile';
import StudentBatch from '../pages/student/StudentBatch';
import StudentApply from '../pages/student/StudentApply';
import StudentCompanies from '../pages/student/StudentCompanies';
import TrainerFeedback from '../pages/student/TrainerFeedback';

// Trainer pages
import TrainerDashboard from '../pages/trainer/TrainerDashboard';
import TrainerBatches from '../pages/trainer/TrainerBatches';
import TrainerBatchDetails from '../pages/trainer/TrainerBatchDetails';
import TrainerStudents from '../pages/trainer/TrainerStudents';
import TrainerResources from '../pages/trainer/TrainerResources';
import StudentFeedback from '../pages/trainer/StudentFeedback';

// Protected route
import ProtectedRoute from './ProtectedRoute';

const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        {/* Public Routes - ensure these come BEFORE protected routes */}
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected Routes */}
        <Route element={<ProtectedRoute requiredRole="ADMIN" />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route path="dashboard" element={<AdminDashboard />} />
            <Route path="students" element={<AdminStudents />} />
            <Route path="batches" element={<AdminBatches />} />
            <Route path="trainers" element={<AdminTrainers />} />
            <Route path="companies" element={<AdminCompanies />} />
            <Route path="feedback" element={<FeedbackSummary />} />
            <Route path="batches/:batchId/feedback" element={<BatchFeedbackDetails />} />
            <Route index element={<Navigate to="/admin/dashboard" replace />} />
          </Route>
        </Route>

        {/* Student Routes */}
        <Route element={<ProtectedRoute requiredRole="STUDENT" />}>
          <Route path="/student" element={<StudentLayout />}>
            <Route path="dashboard" element={<StudentDashboard />} />
            <Route path="profile" element={<StudentProfile />} />
            <Route path="batch" element={<StudentBatch />} />
            <Route path="apply" element={<StudentApply />} />
            <Route path="companies" element={<StudentCompanies />} />
            <Route path="batch/trainers/:trainerId/feedback" element={<TrainerFeedback />} />
            <Route index element={<Navigate to="/student/dashboard" replace />} />
          </Route>
        </Route>

        {/* Trainer Routes */}
        <Route element={<ProtectedRoute requiredRole="TRAINER" />}>
          <Route path="/trainer" element={<TrainerLayout />}>
            <Route path="dashboard" element={<TrainerDashboard />} />
            <Route path="batches" element={<TrainerBatches />} />
            <Route path="batches/:batchId" element={<TrainerBatchDetails />} />
            <Route path="batches/:batchId/students/:studentId/feedback" element={<StudentFeedback />} />
            <Route path="students" element={<TrainerStudents />} />
            <Route path="resources" element={<TrainerResources />} />
            <Route index element={<Navigate to="/trainer/dashboard" replace />} />
          </Route>
        </Route>

        {/* Fallback route for unmatched paths */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;