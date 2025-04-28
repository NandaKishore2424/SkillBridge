import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import { FaUsers, FaLaptopCode, FaChalkboardTeacher, FaBuilding } from 'react-icons/fa';

const adminMenuItems = [
  {
    title: 'Dashboard',
    path: '/admin/dashboard',
    icon: <FaUsers />
  },
  {
    title: 'Students',
    path: '/admin/students',
    icon: <FaUsers />
  },
  {
    title: 'Batches',
    path: '/admin/batches',
    icon: <FaLaptopCode />
  },
  {
    title: 'Trainers',
    path: '/admin/trainers',
    icon: <FaChalkboardTeacher />
  },
  {
    title: 'Companies',
    path: '/admin/companies',
    icon: <FaBuilding />
  }
];

const AdminLayout = () => {
  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar role="admin" menuItems={adminMenuItems} />
      <div className="flex-1 ml-64">
        <Navbar />
        <main className="p-6 mt-16">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default AdminLayout;