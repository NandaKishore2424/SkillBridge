import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';
import CollegeService from '../services/collegeService';

const Register = () => {
  const [selectedRole, setSelectedRole] = useState('STUDENT');
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    department: '',
    registerNumber: '',
    year: '',
    teacherId: '',
    specialization: '',
    bio: '',
    collegeId: ''
  });
  const [colleges, setColleges] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingColleges, setIsLoadingColleges] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchColleges = async () => {
      const response = await CollegeService.listColleges();
      if (response.success) {
        setColleges(response.data);
        if (response.data.length > 0) {
          setFormData(prev => ({ ...prev, collegeId: response.data[0].id }));
        }
      } else {
        setError(response.error || 'Failed to load colleges');
      }
      setIsLoadingColleges(false);
    };

    fetchColleges();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    setError('');
    setSuccess('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setIsLoading(true);

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setIsLoading(false);
      return;
    }

    if (!formData.collegeId) {
      setError('Please select a college');
      setIsLoading(false);
      return;
    }

    try {
      let result;
      if (selectedRole === 'STUDENT') {
        result = await AuthService.registerStudent({
          name: formData.name,
          email: formData.email,
          password: formData.password,
          phoneNumber: formData.phoneNumber,
          department: formData.department,
          registerNumber: formData.registerNumber,
          year: formData.year ? Number(formData.year) : null,
          collegeId: formData.collegeId
        });
      } else {
        result = await AuthService.registerTrainer({
          name: formData.name,
          email: formData.email,
          password: formData.password,
          phoneNumber: formData.phoneNumber,
          department: formData.department,
          teacherId: formData.teacherId,
          specialization: formData.specialization,
          bio: formData.bio,
          collegeId: formData.collegeId
        });
      }

      if (result.success) {
        setSuccess('Registration successful! Redirecting to your dashboard...');
        const targetRoute = selectedRole === 'STUDENT' ? '/student/dashboard' : '/trainer/dashboard';
        setTimeout(() => navigate(targetRoute), 1200);
      } else {
        setError(result.message || 'Registration failed');
      }
    } catch (err) {
      setError('An error occurred during registration.');
    }
    setIsLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-xl w-full space-y-8 bg-white p-10 rounded-lg shadow-md">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">Create your account</h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            Join SkillBridge and start your learning journey
          </p>
        </div>
        {error && <div className="bg-red-100 text-red-700 p-2 rounded">{error}</div>}
        {success && <div className="bg-green-100 text-green-700 p-2 rounded">{success}</div>}
        {isLoadingColleges ? (
          <div className="text-center text-gray-500">Loading colleges...</div>
        ) : (
          <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
            <div className="rounded-md shadow-sm -space-y-px">
              <div className="mb-4">
                <input
                  id="name"
                  name="name"
                  type="text"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Full Name"
                  value={formData.name}
                  onChange={handleChange}
                />
              </div>
              <div className="mb-4">
                <input
                  id="email-address"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Email address"
                  value={formData.email}
                  onChange={handleChange}
                />
              </div>
              <div className="mb-4">
                <input
                  id="phoneNumber"
                  name="phoneNumber"
                  type="tel"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Phone Number"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                />
              </div>
              <div className="mb-4">
                <input
                  id="department"
                  name="department"
                  type="text"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Department"
                  value={formData.department}
                  onChange={handleChange}
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <input
                    id="password"
                    name="password"
                    type="password"
                    autoComplete="new-password"
                    required
                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                  />
                </div>
                <div>
                  <input
                    id="confirmPassword"
                    name="confirmPassword"
                    type="password"
                    autoComplete="new-password"
                    required
                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                    placeholder="Confirm Password"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                  />
                </div>
              </div>
            </div>

            <div className="mt-6">
              <p className="mb-2 text-sm font-medium text-gray-700">I am a:</p>
              <div className="grid grid-cols-2 gap-3">
                {['STUDENT', 'TRAINER'].map(role => (
                  <div
                    key={role}
                    className={`cursor-pointer py-2 px-3 rounded-md flex justify-center items-center ${
                      selectedRole === role
                        ? 'bg-primary-100 border border-primary-500 text-primary-700'
                        : 'bg-gray-100 border border-gray-300 text-gray-700'
                    }`}
                    onClick={() => setSelectedRole(role)}
                  >
                    {role.charAt(0) + role.slice(1).toLowerCase()}
                  </div>
                ))}
              </div>
            </div>

            <div className="border border-dashed border-primary-200 rounded-lg p-4 bg-primary-50/50">
              <p className="text-sm text-gray-700 mb-3">
                Representing a college or training cell?
              </p>
              <button
                type="button"
                onClick={() => navigate('/college/signup')}
                className="w-full border border-primary-500 text-primary-700 font-medium py-2 rounded-md hover:bg-primary-500 hover:text-white transition"
              >
                Register a College Instead
              </button>
            </div>

            {selectedRole === 'STUDENT' ? (
              <div className="space-y-4">
                <input
                  id="registerNumber"
                  name="registerNumber"
                  type="text"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Register Number"
                  value={formData.registerNumber}
                  onChange={handleChange}
                />
                <input
                  id="year"
                  name="year"
                  type="number"
                  min="1"
                  max="4"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Year of Study"
                  value={formData.year}
                  onChange={handleChange}
                />
              </div>
            ) : (
              <div className="space-y-4">
                <input
                  id="teacherId"
                  name="teacherId"
                  type="text"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Teacher ID"
                  value={formData.teacherId}
                  onChange={handleChange}
                />
                <input
                  id="specialization"
                  name="specialization"
                  type="text"
                  required
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Specialization"
                  value={formData.specialization}
                  onChange={handleChange}
                />
                <textarea
                  id="bio"
                  name="bio"
                  rows="3"
                  className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                  placeholder="Short Bio (optional)"
                  value={formData.bio}
                  onChange={handleChange}
                />
              </div>
            )}

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">College</label>
              <select
                name="collegeId"
                className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-primary-500 focus:border-primary-500"
                value={formData.collegeId}
                onChange={handleChange}
                required
              >
                {colleges.map(college => (
                  <option key={college.id} value={college.id}>
                    {college.name} ({college.domain})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <button
                type="submit"
                disabled={isLoading}
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
              >
                {isLoading ? 'Creating Account...' : 'Create Account'}
              </button>
            </div>
          </form>
        )}
        <div className="text-center mt-4 space-y-2">
          <p className="text-sm text-gray-600">
            Already have an account?{' '}
            <Link to="/login" className="font-medium text-primary-600 hover:text-primary-500">
              Sign in
            </Link>
          </p>
          <p className="text-sm text-gray-600">
            Are you a college admin?{' '}
            <Link to="/college/signup" className="font-medium text-primary-600 hover:text-primary-500">
              Register your college
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;