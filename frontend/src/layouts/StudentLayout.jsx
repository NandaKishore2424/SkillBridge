import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import { FaUserCircle, FaLaptopCode, FaCertificate, FaBuilding } from 'react-icons/fa';

const studentMenuItems = [
  {
    title: 'Dashboard',
    path: '/student/dashboard',
    icon: <FaUserCircle />
  },
  {
    title: 'Profile',
    path: '/student/profile',
    icon: <FaUserCircle />
  },
  {
    title: 'Current Batch',
    path: '/student/batch',
    icon: <FaLaptopCode />
  },
  {
    title: 'Apply for Batch',
    path: '/student/apply',
    icon: <FaCertificate />
  },
  {
    title: 'Companies',
    path: '/student/companies',
    icon: <FaBuilding />
  }
];

const StudentLayout = () => {
  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar role="student" menuItems={studentMenuItems} />
      <div className="flex-1 ml-64">
        <Navbar />
        <main className="p-6 mt-16">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default StudentLayout;