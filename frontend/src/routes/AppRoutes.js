import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
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
import AdminReports from '../pages/admin/AdminReports';
import AddStudent from '../pages/admin/AddStudent';
import AddBatch from '../pages/admin/AddBatch';
import AddTrainer from '../pages/admin/AddTrainer';
import AddCompany from '../pages/admin/AddCompany';

// Student pages
import StudentDashboard from '../pages/student/StudentDashboard';
import StudentProfile from '../pages/student/StudentProfile';
import StudentBatch from '../pages/student/StudentBatch';
import StudentFeedback from '../pages/student/StudentFeedback';

// Trainer pages
import TrainerDashboard from '../pages/trainer/TrainerDashboard';
import TrainerBatches from '../pages/trainer/TrainerBatches';
import TrainerStudents from '../pages/trainer/TrainerStudents';
import TrainerSyllabus from '../pages/trainer/TrainerSyllabus';

// Protected route component
import ProtectedRoute from './ProtectedRoute';

function AppRoutes() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route element={<ProtectedRoute requiredRole="ADMIN" />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route path="dashboard" element={<AdminDashboard />} />
            <Route path="students" element={<AdminStudents />} />
            <Route path="students/add" element={<AddStudent />} />
            <Route path="batches" element={<AdminBatches />} />
            <Route path="batches/add" element={<AddBatch />} />
            <Route path="trainers" element={<AdminTrainers />} />
            <Route path="trainers/add" element={<AddTrainer />} />
            <Route path="companies" element={<AdminCompanies />} />
            <Route path="companies/add" element={<AddCompany />} />
            <Route path="reports" element={<AdminReports />} />
            <Route index element={<Navigate to="/admin/dashboard" replace />} />
          </Route>
        </Route>

        <Route element={<ProtectedRoute requiredRole="STUDENT" />}>
          <Route path="/student" element={<StudentLayout />}>
            <Route path="dashboard" element={<StudentDashboard />} />
            <Route path="profile" element={<StudentProfile />} />
            <Route path="batch" element={<StudentBatch />} />
            <Route path="feedback" element={<StudentFeedback />} />
            <Route index element={<Navigate to="/student/dashboard" replace />} />
          </Route>
        </Route>

        <Route element={<ProtectedRoute requiredRole="TRAINER" />}>
          <Route path="/trainer" element={<TrainerLayout />}>
            <Route path="dashboard" element={<TrainerDashboard />} />
            <Route path="batches" element={<TrainerBatches />} />
            <Route path="students" element={<TrainerStudents />} />
            <Route path="syllabus" element={<TrainerSyllabus />} />
            <Route index element={<Navigate to="/trainer/dashboard" replace />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}

export default AppRoutes;