import { Navigate, Outlet } from 'react-router-dom';
import { useEffect, useState } from 'react';
import AuthService from '../services/authService';

const ProtectedRoute = ({ requiredRole }) => {
  const [isChecking, setIsChecking] = useState(true);
  const [isAuthorized, setIsAuthorized] = useState(false);

  useEffect(() => {
    let isMounted = true;

    const checkAuth = async () => {
      console.log('🔒 ProtectedRoute checking auth for role:', requiredRole);
      const session = await AuthService.ensureSession();
      console.log('🔒 Session result:', session);
      if (!isMounted) return;

      const hasRequiredRole = requiredRole
        ? session.user && session.user.role === requiredRole
        : true;

      console.log('🔒 Has required role:', hasRequiredRole, 'Authenticated:', session.authenticated);
      setIsAuthorized(session.authenticated && hasRequiredRole);
      setIsChecking(false);
    };

    checkAuth();
    return () => {
      isMounted = false;
    };
  }, [requiredRole]);

  if (isChecking) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary-500"></div>
      </div>
    );
  }

  if (!isAuthorized) {
    console.log('🔒 Not authorized, redirecting to /login');
    return <Navigate to="/login" />;
  }
  
  console.log('🔒 Authorized, rendering protected content');
  return <Outlet />;
};

export default ProtectedRoute;