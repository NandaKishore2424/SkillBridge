import { Navigate, Outlet } from 'react-router-dom';
import { useEffect, useState } from 'react';
import AuthService from '../services/authService';

const ProtectedRoute = ({ requiredRole }) => {
  const [isChecking, setIsChecking] = useState(true);
  const [isAuthorized, setIsAuthorized] = useState(false);
  
  useEffect(() => {
    const checkAuth = () => {
      const isAuthenticated = AuthService.isAuthenticated();
      const hasRequiredRole = requiredRole 
        ? AuthService.hasRole(requiredRole) 
        : true;
      
      setIsAuthorized(isAuthenticated && hasRequiredRole);
      setIsChecking(false);
    };
    
    checkAuth();
  }, [requiredRole]);
  
  if (isChecking) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary-500"></div>
      </div>
    );
  }
  
  return isAuthorized ? <Outlet /> : <Navigate to="/login" />;
};

export default ProtectedRoute;