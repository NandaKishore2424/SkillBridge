import ApiService from './apiService';

const CollegeService = {
  listColleges: async () => {
    const response = await ApiService.get('/colleges');
    return response;
  }
};

export default CollegeService;

