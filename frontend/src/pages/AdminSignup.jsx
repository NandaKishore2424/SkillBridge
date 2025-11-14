import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';

const AdminSignup = () => {
  const [adminData, setAdminData] = useState({
    adminName: '',
    adminEmail: '',
    phoneNumber: '',
    roleTitle: '',
    password: '',
    confirmPassword: ''
  });

  const [collegeData, setCollegeData] = useState({
    collegeName: '',
    collegeDomain: '',
    collegeWebsite: '',
    collegeContactEmail: '',
    collegeContactPhone: '',
    collegeAddress: ''
  });

  const [status, setStatus] = useState({ error: '', success: '' });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  const handleAdminChange = (e) => {
    setAdminData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    setStatus({ error: '', success: '' });
  };

  const handleCollegeChange = (e) => {
    setCollegeData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    setStatus({ error: '', success: '' });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus({ error: '', success: '' });

    if (adminData.password !== adminData.confirmPassword) {
      setStatus({ error: 'Passwords do not match', success: '' });
      return;
    }

    setIsSubmitting(true);
    try {
      const payload = {
        adminName: adminData.adminName,
        adminEmail: adminData.adminEmail,
        phoneNumber: adminData.phoneNumber,
        roleTitle: adminData.roleTitle,
        password: adminData.password,
        collegeName: collegeData.collegeName,
        collegeDomain: collegeData.collegeDomain,
        collegeWebsite: collegeData.collegeWebsite,
        collegeContactEmail: collegeData.collegeContactEmail,
        collegeContactPhone: collegeData.collegeContactPhone,
        collegeAddress: collegeData.collegeAddress
      };

      const result = await AuthService.registerAdmin(payload);
      if (result.success) {
        await AuthService.logout();
        setStatus({ success: 'College registered successfully! Redirecting to login...', error: '' });
        setTimeout(() => navigate('/login', { state: { message: 'College registered! Please log in as admin.' } }), 1500);
      } else {
        setStatus({ error: result.message || 'Registration failed', success: '' });
      }
    } catch (error) {
      setStatus({ error: 'An error occurred during registration.', success: '' });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-3xl mx-auto bg-white shadow-lg rounded-xl p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-extrabold text-gray-900">College Admin Registration</h1>
          <p className="mt-2 text-gray-600">
            Register your college to onboard students and trainers on SkillBridge.
          </p>
        </div>

        {status.error && <div className="mb-4 bg-red-100 text-red-700 p-3 rounded">{status.error}</div>}
        {status.success && <div className="mb-4 bg-green-100 text-green-700 p-3 rounded">{status.success}</div>}

        <form className="space-y-8" onSubmit={handleSubmit}>
          <section>
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Admin Information</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input
                name="adminName"
                type="text"
                required
                placeholder="Full Name"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={adminData.adminName}
                onChange={handleAdminChange}
              />
              <input
                name="adminEmail"
                type="email"
                required
                placeholder="Work Email"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={adminData.adminEmail}
                onChange={handleAdminChange}
              />
              <input
                name="phoneNumber"
                type="tel"
                required
                placeholder="Phone Number"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={adminData.phoneNumber}
                onChange={handleAdminChange}
              />
              <input
                name="roleTitle"
                type="text"
                required
                placeholder="Role (e.g., Program Director)"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={adminData.roleTitle}
                onChange={handleAdminChange}
              />
              <input
                name="password"
                type="password"
                required
                placeholder="Password"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={adminData.password}
                onChange={handleAdminChange}
              />
              <input
                name="confirmPassword"
                type="password"
                required
                placeholder="Confirm Password"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={adminData.confirmPassword}
                onChange={handleAdminChange}
              />
            </div>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-800 mb-4">College Details</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input
                name="collegeName"
                type="text"
                required
                placeholder="College Name"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={collegeData.collegeName}
                onChange={handleCollegeChange}
              />
              <input
                name="collegeDomain"
                type="text"
                required
                placeholder="College Domain (e.g., mycollege.edu)"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={collegeData.collegeDomain}
                onChange={handleCollegeChange}
              />
              <input
                name="collegeWebsite"
                type="url"
                placeholder="College Website"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={collegeData.collegeWebsite}
                onChange={handleCollegeChange}
              />
              <input
                name="collegeContactEmail"
                type="email"
                placeholder="Admissions Email"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={collegeData.collegeContactEmail}
                onChange={handleCollegeChange}
              />
              <input
                name="collegeContactPhone"
                type="tel"
                placeholder="Admissions Phone"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={collegeData.collegeContactPhone}
                onChange={handleCollegeChange}
              />
              <textarea
                name="collegeAddress"
                rows="3"
                placeholder="Campus Address"
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500 md:col-span-2"
                value={collegeData.collegeAddress}
                onChange={handleCollegeChange}
              />
            </div>
          </section>

          <div>
            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
            >
              {isSubmitting ? 'Registering...' : 'Register College'}
            </button>
          </div>
        </form>

        <div className="text-center mt-6">
          <p className="text-sm text-gray-600">
            Already have an admin account?{' '}
            <Link to="/login" className="font-medium text-primary-600 hover:text-primary-500">
              Sign in here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default AdminSignup;

