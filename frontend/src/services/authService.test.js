// filepath: e:\Project - 2025\PlacePrep\skillbridge\frontend\src\services\authService.test.js
import AuthService from './authService';
import axios from 'axios';

// Mock axios
jest.mock('axios');

describe('AuthService', () => {
  // Setup localStorage mock
  const localStorageMock = (() => {
    let store = {};
    return {
      getItem: jest.fn(key => store[key]),
      setItem: jest.fn((key, value) => {
        store[key] = value;
      }),
      removeItem: jest.fn(key => {
        delete store[key];
      }),
      clear: jest.fn(() => {
        store = {};
      })
    };
  })();
  
  Object.defineProperty(window, 'localStorage', {
    value: localStorageMock
  });
  
  beforeEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
  });
  
  test('login - successful', async () => {
    const mockResponse = {
      data: {
        token: 'mock-token',
        id: '1',
        email: 'test@example.com',
        name: 'Test User',
        role: 'STUDENT'
      }
    };
    
    axios.post.mockResolvedValue(mockResponse);
    
    const result = await AuthService.login('test@example.com', 'password');
    
    expect(result.success).toBe(true);
    expect(result.user).toEqual(mockResponse.data);
    expect(localStorage.setItem).toHaveBeenCalledWith('token', 'mock-token');
    expect(localStorage.setItem).toHaveBeenCalledWith('user', JSON.stringify({
      id: '1',
      email: 'test@example.com',
      name: 'Test User',
      role: 'STUDENT'
    }));
  });
  
  test('login - failed', async () => {
    axios.post.mockRejectedValue({
      response: {
        data: {
          message: 'Invalid credentials'
        }
      }
    });
    
    const result = await AuthService.login('wrong@example.com', 'wrong');
    
    expect(result.success).toBe(false);
    expect(result.message).toBe('Invalid credentials');
    expect(localStorage.setItem).not.toHaveBeenCalled();
  });
});