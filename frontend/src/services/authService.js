import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/auth';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add a request interceptor to include token in all requests
apiClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// Add a response interceptor for error handling
apiClient.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;
    
    // If error is 401 (Unauthorized) and not already retrying
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        // Try to get a new token (refresh token logic)
        // You'll need to implement this endpoint in your backend
        const response = await apiClient.post('/refresh-token', {
          refreshToken: localStorage.getItem('refreshToken')
        });
        
        const { token } = response.data;
        localStorage.setItem('token', token);
        
        // Retry the original request
        originalRequest.headers['Authorization'] = `Bearer ${token}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        // If refresh fails, logout user
        AuthService.logout();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);

const AuthService = {
  login: async (email, password) => {
    try {
      console.log("Attempting login with:", { email, password });
      
      const response = await apiClient.post('/login', { email, password });
      console.log("Login response:", response);
      
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('refreshToken', response.data.refreshToken || '');
        localStorage.setItem('user', JSON.stringify({
          id: response.data.id,
          email: response.data.email,
          name: response.data.name,
          role: response.data.role
        }));
        
        return { success: true, user: response.data };
      } else {
        return { 
          success: false, 
          message: response.data.message || 'Login failed'
        };
      }
    } catch (error) {
      console.error("Login error:", error);
      
      // TEMPORARY MOCK AUTH - for testing when backend is not available
      if (email === 'admin@skillbridge.com' && password === 'admin123') {
        const mockUser = {
          id: '1',
          email: email,
          name: 'Admin User',
          role: 'ADMIN'
        };
        
        localStorage.setItem('user', JSON.stringify(mockUser));
        localStorage.setItem('token', 'mock-token');
        return { success: true, user: mockUser };
      }

      if (email === 'student@test.com' && password === 'password') {
        const mockUser = {
          id: '2',
          email: email,
          name: 'Student User',
          role: 'STUDENT'
        };
        
        localStorage.setItem('user', JSON.stringify(mockUser));
        localStorage.setItem('token', 'mock-token');
        return { success: true, user: mockUser };
      }

      if (email === 'john.smith@example.com' && password === 'password') {
        const mockUser = {
          id: '3',
          email: email,
          name: 'John Smith',
          role: 'TRAINER'
        };
        
        localStorage.setItem('user', JSON.stringify(mockUser));
        localStorage.setItem('token', 'mock-token');
        return { success: true, user: mockUser };
      }
      
      const message = error.response?.data?.message || 
                    'Unable to connect to server. Please try again later.';
      return { success: false, message };
    }
  },

  register: async (userData) => {
    try {
      const response = await apiClient.post('/register', userData);
      return { 
        success: true, 
        user: response.data 
      };
    } catch (error) {
      const message = error.response?.data?.message || 
                     error.response?.status === 409 ? 
                     'User with this email already exists' : 
                     'Registration failed. Please try again.';
      return { success: false, message };
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  },
  
  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    if (!userStr) return null;
    return JSON.parse(userStr);
  },
  
  isAuthenticated: () => {
    const token = localStorage.getItem('token');
    if (!token) return false;
    
    // Check if token is expired
    try {
      // For JWT tokens
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > new Date().getTime();
    } catch {
      return false;
    }
  },
  
  getToken: () => {
    return localStorage.getItem('token');
  },
  
  hasRole: (requiredRole) => {
    const user = AuthService.getCurrentUser();
    return user && user.role === requiredRole;
  }
};

export default AuthService;