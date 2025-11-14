import axios from 'axios';

const API_URL = `${process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1'}/auth`;
const USER_STORAGE_KEY = 'user';

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
});

const saveUserProfile = (profile) => {
  if (!profile) {
    localStorage.removeItem(USER_STORAGE_KEY);
    return;
  }
  localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(profile));
};

const parseErrorMessage = (error, fallback) =>
  error.response?.data?.message || fallback;

const AuthService = {
  login: async (email, password) => {
    try {
      const response = await apiClient.post('/login', { email, password });
      saveUserProfile(response.data);
      return { success: true, user: response.data };
    } catch (error) {
      return {
        success: false,
        message: parseErrorMessage(error, 'Unable to authenticate. Please try again.')
      };
    }
  },

  registerStudent: async (studentData) => {
    try {
      const response = await apiClient.post('/student/register', studentData);
      saveUserProfile(response.data);
      return { success: true, user: response.data };
    } catch (error) {
      return {
        success: false,
        message: parseErrorMessage(error, 'Student registration failed. Please try again.')
      };
    }
  },

  registerTrainer: async (trainerData) => {
    try {
      const response = await apiClient.post('/trainer/register', trainerData);
      saveUserProfile(response.data);
      return { success: true, user: response.data };
    } catch (error) {
      return {
        success: false,
        message: parseErrorMessage(error, 'Trainer registration failed. Please try again.')
      };
    }
  },

  registerAdmin: async (adminData) => {
    try {
      const response = await apiClient.post('/admin/register', adminData);
      saveUserProfile(response.data);
      return { success: true, user: response.data };
    } catch (error) {
      return {
        success: false,
        message: parseErrorMessage(error, 'Admin registration failed. Please try again.')
      };
    }
  },

  fetchProfile: async () => {
    const response = await apiClient.get('/me');
    saveUserProfile(response.data);
    return response.data;
  },

  refreshSession: async () => {
    const response = await apiClient.post('/refresh');
    saveUserProfile(response.data);
    return response.data;
  },

  logout: async () => {
    try {
      await apiClient.post('/logout');
    } catch (error) {
      console.warn('Logout request failed', error);
    } finally {
      saveUserProfile(null);
    }
  },
  
  getCurrentUser: () => {
    const userStr = localStorage.getItem(USER_STORAGE_KEY);
    if (!userStr) return null;
    try {
      return JSON.parse(userStr);
    } catch {
      return null;
    }
  },

  ensureSession: async () => {
    const cachedUser = AuthService.getCurrentUser();
    if (cachedUser) {
      return { authenticated: true, user: cachedUser };
    }

    try {
      const profile = await AuthService.fetchProfile();
      return { authenticated: true, user: profile };
    } catch (error) {
      saveUserProfile(null);
      return { authenticated: false, user: null };
    }
  },

  hasRole: (requiredRole) => {
    const user = AuthService.getCurrentUser();
    return !!user && user.role === requiredRole;
  }
};

export default AuthService;