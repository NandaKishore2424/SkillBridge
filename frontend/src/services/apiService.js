import axios from 'axios';
import AuthService from './authService';

const BASE_URL =
  process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

// Create axios instance
const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add auth token to every request
api.interceptors.request.use(
  config => {
    const token = AuthService.getToken();
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Handle response errors
api.interceptors.response.use(
  response => response,
  error => {
    // If unauthorized and not on auth pages, redirect to login
    if (error.response?.status === 401 && 
        !window.location.pathname.includes('/login') && 
        !window.location.pathname.includes('/register')) {
      AuthService.logout();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Generic API service with error handling
const ApiService = {
  // Generic GET with error handling
  get: async (endpoint, params = {}) => {
    try {
      const response = await api.get(endpoint, { params });
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error(`Error fetching ${endpoint}:`, error);
      return {
        success: false,
        error: error.response?.data?.message || 'Failed to fetch data'
      };
    }
  },

  // Generic POST with error handling
  post: async (endpoint, data = {}) => {
    try {
      const response = await api.post(endpoint, data);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error(`Error posting to ${endpoint}:`, error);
      return {
        success: false,
        error: error.response?.data?.message || 'Failed to save data'
      };
    }
  },

  // Generic PUT with error handling
  put: async (endpoint, data = {}) => {
    try {
      const response = await api.put(endpoint, data);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error(`Error updating ${endpoint}:`, error);
      return {
        success: false,
        error: error.response?.data?.message || 'Failed to update data'
      };
    }
  },

  // Generic DELETE with error handling
  delete: async (endpoint) => {
    try {
      const response = await api.delete(endpoint);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      console.error(`Error deleting ${endpoint}:`, error);
      return {
        success: false,
        error: error.response?.data?.message || 'Failed to delete data'
      };
    }
  }
};

export default ApiService;