import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Sidebar = ({ role, menuItems }) => {
  const location = useLocation();

  return (
    <div className="bg-gray-800 text-white w-64 min-h-screen fixed top-0 left-0 overflow-y-auto pt-16 z-10">
      <div className="px-4 py-6">
        <h2 className="text-xl font-semibold capitalize">{role} Dashboard</h2>
      </div>
      
      <nav className="mt-4">
        <ul className="space-y-1">
          {menuItems.map((item, index) => (
            <li key={index}>
              <Link
                to={item.path}
                className={`flex items-center px-4 py-3 hover:bg-gray-700 ${
                  location.pathname === item.path ? 'bg-gray-700' : ''
                }`}
              >
                <span className="mr-3">{item.icon}</span>
                <span>{item.title}</span>
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
};

export default Sidebar;