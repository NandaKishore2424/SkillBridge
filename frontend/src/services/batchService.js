import ApiService from './apiService';

const BatchService = {
  getBatches: (page = 0, size = 10) => {
    return ApiService.get('/batches', { page, size });
  },
  
  getBatchById: (id) => {
    return ApiService.get(`/batches/${id}`);
  },
  
  createBatch: (batchData) => {
    return ApiService.post('/batches', batchData);
  },
  
  updateBatch: (id, batchData) => {
    return ApiService.put(`/batches/${id}`, batchData);
  },
  
  deleteBatch: (id) => {
    return ApiService.delete(`/batches/${id}`);
  },
  
  addStudentToBatch: (batchId, studentId) => {
    return ApiService.post(`/batches/${batchId}/students/${studentId}`);
  },
  
  removeStudentFromBatch: (batchId, studentId) => {
    return ApiService.delete(`/batches/${batchId}/students/${studentId}`);
  },
  
  updateBatchSyllabus: (batchId, syllabusData) => {
    return ApiService.put(`/batches/${batchId}/syllabus`, syllabusData);
  }
};

export default BatchService;