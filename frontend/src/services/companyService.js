import ApiService from './apiService';

const CompanyService = {
  getCompanies: (page = 0, size = 10) => {
    return ApiService.get('/companies', { page, size });
  },
  
  getCompanyById: (id) => {
    return ApiService.get(`/companies/${id}`);
  },
  
  createCompany: (companyData) => {
    return ApiService.post('/companies', companyData);
  },
  
  updateCompany: (id, companyData) => {
    return ApiService.put(`/companies/${id}`, companyData);
  },
  
  deleteCompany: (id) => {
    return ApiService.delete(`/companies/${id}`);
  }
};

export default CompanyService;