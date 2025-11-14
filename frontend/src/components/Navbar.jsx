import React from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/authService';

const Navbar = () => {
  const navigate = useNavigate();
  const user = AuthService.getCurrentUser();

  const handleLogout = async () => {
    await AuthService.logout();
    navigate('/login');
  };

  return (
    <nav className="bg-white border-b border-gray-200 fixed w-full z-30 shadow-sm">
      <div className="px-4 py-3 flex justify-between items-center">
        <div className="flex items-center">
          <span className="text-xl font-semibold text-primary-600">SkillBridge</span>
        </div>

        <div className="flex items-center space-x-4">
          {/* User profile dropdown */}
          <div className="relative">
            <div className="flex items-center space-x-3 cursor-pointer">
              <div className="h-8 w-8 rounded-full bg-primary-100 flex items-center justify-center text-primary-700 font-bold">
                {user?.email?.charAt(0).toUpperCase() || 'U'}
              </div>
              <div>
                <p className="text-sm font-medium">{user?.email || 'User'}</p>
                <p className="text-xs text-gray-500">{user?.role || 'Role'}</p>
              </div>
            </div>
          </div>
          
          {/* Logout Button */}
          <button 
            onClick={handleLogout}
            className="text-sm text-gray-600 hover:text-primary-600"
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;