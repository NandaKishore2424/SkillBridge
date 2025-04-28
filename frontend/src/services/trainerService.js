import ApiService from './apiService';

const TrainerService = {
  getTrainers: (page = 0, size = 10) => {
    return ApiService.get('/trainers', { page, size });
  },
  
  getTrainerById: (id) => {
    return ApiService.get(`/trainers/${id}`);
  },
  
  createTrainer: (trainerData) => {
    return ApiService.post('/trainers', trainerData);
  },
  
  updateTrainer: (id, trainerData) => {
    return ApiService.put(`/trainers/${id}`, trainerData);
  },
  
  deleteTrainer: (id) => {
    return ApiService.delete(`/trainers/${id}`);
  },
  
  getTrainerBatches: (trainerId) => {
    return ApiService.get(`/trainers/${trainerId}/batches`);
  },
  
  getTrainerStudents: (trainerId) => {
    return ApiService.get(`/trainers/${trainerId}/students`);
  }
};

export default TrainerService;