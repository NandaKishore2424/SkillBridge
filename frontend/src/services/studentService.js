import ApiService from './apiService';

const StudentService = {
  getStudents: (page = 0, size = 10) => {
    return ApiService.get('/students', { page, size });
  },
  
  getStudentById: (id) => {
    return ApiService.get(`/students/${id}`);
  },
  
  getCurrentStudentProfile: () => {
    return ApiService.get('/students/profile');
  },
  
  updateStudentProfile: (studentData) => {
    return ApiService.put('/students/profile', studentData);
  },
  
  addSkill: (skillData) => {
    return ApiService.post('/students/skills', skillData);
  },
  
  removeSkill: (skillId) => {
    return ApiService.delete(`/students/skills/${skillId}`);
  },
  
  addProject: (projectData) => {
    return ApiService.post('/students/projects', projectData);
  },
  
  updateProject: (projectId, projectData) => {
    return ApiService.put(`/students/projects/${projectId}`, projectData);
  },
  
  removeProject: (projectId) => {
    return ApiService.delete(`/students/projects/${projectId}`);
  },

  // Add this function to the StudentService object

  createStudent: (studentData) => {
    return ApiService.post('/students', studentData);
  },
};

export default StudentService;