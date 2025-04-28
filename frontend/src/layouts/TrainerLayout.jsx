import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import { FaChalkboardTeacher, FaUsers, FaBookOpen, FaClipboardList } from 'react-icons/fa';

const trainerMenuItems = [
  {
    title: 'Dashboard',
    path: '/trainer/dashboard',
    icon: <FaChalkboardTeacher />
  },
  {
    title: 'My Batches',
    path: '/trainer/batches',
    icon: <FaClipboardList />
  },
  {
    title: 'Students',
    path: '/trainer/students',
    icon: <FaUsers />
  },
  {
    title: 'Resources',
    path: '/trainer/resources',
    icon: <FaBookOpen />
  }
];

const TrainerLayout = () => {
  return (
    <div className="flex h-screen bg-gray-100">
      <Sidebar role="trainer" menuItems={trainerMenuItems} />
      <div className="flex-1 ml-64">
        <Navbar />
        <main className="p-6 mt-16">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default TrainerLayout;