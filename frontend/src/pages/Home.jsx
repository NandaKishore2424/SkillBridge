import React from 'react';
import { Link } from 'react-router-dom';
import { FaGraduationCap, FaLaptopCode, FaUserTie, FaChartLine, FaArrowRight } from 'react-icons/fa';

const Home = () => {
  return (
    <div className="min-h-screen bg-white">
      {/* Navigation */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <Link to="/" className="flex-shrink-0 flex items-center">
                <span className="text-2xl font-bold text-primary-600">SkillBridge</span>
              </Link>
            </div>
            <div className="flex items-center space-x-4">
              <Link to="/login" className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-primary-600 transition">
                Login
              </Link>
              <Link to="/register" className="px-4 py-2 rounded-md text-sm font-medium bg-primary-600 text-white hover:bg-primary-700 transition">
                Register
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero section */}
      <div className="relative bg-gradient-to-r from-primary-600 to-primary-800 overflow-hidden">
        <div className="absolute inset-y-0 right-0 w-1/2 bg-primary-700 transform -skew-x-12 origin-top-right"></div>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 relative">
          <div className="max-w-3xl">
            <h1 className="text-4xl font-extrabold text-white sm:text-5xl sm:tracking-tight lg:text-6xl">
              Bridge the gap between skills and opportunity
            </h1>
            <p className="mt-6 text-xl text-primary-100 max-w-2xl">
              SkillBridge connects students with industry-relevant training, expert mentors, and hiring companies - all in one platform.
            </p>
            <div className="mt-10 flex space-x-4">
              <Link
                to="/register"
                className="px-6 py-3 border border-transparent text-base font-medium rounded-md text-primary-700 bg-white hover:bg-gray-50 transition"
              >
                Get Started
              </Link>
              <Link
                to="/login"
                className="px-6 py-3 border border-white text-base font-medium rounded-md text-white hover:bg-primary-700 transition"
              >
                Sign In
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Features section */}
      <div className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h2 className="text-3xl font-extrabold text-gray-900">Why Choose SkillBridge?</h2>
            <p className="mt-4 max-w-2xl text-xl text-gray-500 mx-auto">
              Our platform offers a comprehensive approach to skill development and career advancement
            </p>
          </div>

          <div className="mt-16">
            <div className="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-4">
              <div className="bg-white rounded-lg shadow-md p-6 border-t-4 border-primary-500 hover:shadow-lg transition">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-primary-100 text-primary-700 mb-4">
                  <FaGraduationCap className="h-6 w-6" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900">Personalized Learning</h3>
                <p className="mt-3 text-gray-600">
                  Get recommended batches based on your skills and career goals
                </p>
              </div>
              
              <div className="bg-white rounded-lg shadow-md p-6 border-t-4 border-green-500 hover:shadow-lg transition">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-green-100 text-green-700 mb-4">
                  <FaLaptopCode className="h-6 w-6" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900">Industry-Relevant Skills</h3>
                <p className="mt-3 text-gray-600">
                  Learn technologies and frameworks actively sought by top companies
                </p>
              </div>
              
              <div className="bg-white rounded-lg shadow-md p-6 border-t-4 border-purple-500 hover:shadow-lg transition">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-purple-100 text-purple-700 mb-4">
                  <FaUserTie className="h-6 w-6" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900">Expert Mentors</h3>
                <p className="mt-3 text-gray-600">
                  Learn from experienced professionals with real-world expertise
                </p>
              </div>
              
              <div className="bg-white rounded-lg shadow-md p-6 border-t-4 border-yellow-500 hover:shadow-lg transition">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-yellow-100 text-yellow-700 mb-4">
                  <FaChartLine className="h-6 w-6" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900">Career Opportunities</h3>
                <p className="mt-3 text-gray-600">
                  Get connected with companies hiring for skills you've mastered
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* CTA section */}
      <div className="bg-primary-700">
        <div className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8 lg:py-16 flex flex-col md:flex-row md:items-center md:justify-between">
          <div>
            <h2 className="text-3xl font-extrabold tracking-tight text-white md:text-4xl">
              Ready to start your journey?
            </h2>
            <p className="mt-3 max-w-3xl text-lg text-primary-200">
              Join thousands of students already using SkillBridge to advance their careers.
            </p>
          </div>
          <div className="mt-8 flex lg:mt-0 lg:flex-shrink-0">
            <div className="inline-flex rounded-md shadow">
              <Link
                to="/register"
                className="inline-flex items-center justify-center px-5 py-3 border border-transparent text-base font-medium rounded-md text-primary-700 bg-white hover:bg-gray-50"
              >
                Create account <FaArrowRight className="ml-2 -mr-1 h-4 w-4" />
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="bg-gray-800">
        <div className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col items-center">
            <div className="text-white text-xl font-bold">SkillBridge</div>
            <p className="mt-2 text-sm text-gray-400">
              Connecting students, trainers, and companies
            </p>
            <div className="mt-8 text-sm text-gray-400">
              &copy; {new Date().getFullYear()} SkillBridge. All rights reserved.
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Home;