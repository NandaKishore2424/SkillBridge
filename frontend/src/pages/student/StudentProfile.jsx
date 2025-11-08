import React, { useState, useEffect } from 'react';
import { FaPlus, FaTimes, FaGithub, FaGlobe, FaFileAlt } from 'react-icons/fa';
import StudentService from '../../services/studentService';

const StudentProfile = () => {
  const [student, setStudent] = useState(null);
  const [skills, setSkills] = useState([]);
  const [projects, setProjects] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState({});
  const [newSkill, setNewSkill] = useState({ name: '', level: 'BEGINNER' });
  const [newProject, setNewProject] = useState({ title: '', description: '', techStack: '' });
  const [isAddingSkill, setIsAddingSkill] = useState(false);
  const [isAddingProject, setIsAddingProject] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  // Fetch student profile data
  useEffect(() => {
    const fetchProfile = async () => {
      setIsLoading(true);
      setError('');
      
      try {
        const response = await StudentService.getCurrentStudentProfile();
        
        if (response.success) {
          setStudent(response.data);
          setSkills(response.data.skills || []);
          setProjects(response.data.projects || []);
        } else {
          setError(response.error || 'Failed to load profile data');
        }
      } catch (err) {
        setError('An error occurred while fetching profile data');
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };
    
    fetchProfile();
  }, []);

  useEffect(() => {
    if (student) {
      setEditData({ ...student });
    }
  }, [student]);

  const handleEditToggle = () => {
    if (isEditing) {
      // Reset to original data if canceling
      setEditData({ ...student });
    }
    setIsEditing(!isEditing);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditData({
      ...editData,
      [name]: value
    });
  };

  const handleProfileUpdate = async () => {
    setIsLoading(true);
    setError('');
    setSuccessMessage('');
    
    try {
      const response = await StudentService.updateStudentProfile(editData);
      
      if (response.success) {
        setStudent(response.data);
        setSuccessMessage('Profile updated successfully!');
        setIsEditing(false);
      } else {
        setError(response.error || 'Failed to update profile');
      }
    } catch (err) {
      setError('An error occurred while updating profile');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleNewSkillChange = (e) => {
    const { name, value } = e.target;
    setNewSkill({
      ...newSkill,
      [name]: value
    });
  };

  const handleAddSkill = async () => {
    if (!newSkill.name.trim()) {
      setError("Skill name cannot be empty");
      return;
    }

    setIsLoading(true);
    setError('');
    setSuccessMessage('');
    
    try {
      const response = await StudentService.addSkill(newSkill);
      
      if (response.success) {
        setSkills([...skills, response.data]);
        setNewSkill({ name: '', level: 'BEGINNER' });
        setIsAddingSkill(false);
        setSuccessMessage('Skill added successfully!');
      } else {
        setError(response.error || 'Failed to add skill');
      }
    } catch (err) {
      setError('An error occurred while adding skill');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRemoveSkill = async (skillId) => {
    setIsLoading(true);
    setError('');
    setSuccessMessage('');
    
    try {
      const response = await StudentService.removeSkill(skillId);
      
      if (response.success) {
        setSkills(skills.filter(skill => skill.id !== skillId));
        setSuccessMessage('Skill removed successfully!');
      } else {
        setError(response.error || 'Failed to remove skill');
      }
    } catch (err) {
      setError('An error occurred while removing skill');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleNewProjectChange = (e) => {
    const { name, value } = e.target;
    setNewProject({
      ...newProject,
      [name]: value
    });
  };

  const handleAddProject = async () => {
    if (!newProject.title.trim() || !newProject.description.trim()) {
      setError("Project title and description are required");
      return;
    }

    setIsLoading(true);
    setError('');
    setSuccessMessage('');
    
    try {
      const response = await StudentService.addProject(newProject);
      
      if (response.success) {
        setProjects([...projects, response.data]);
        setNewProject({ title: '', description: '', techStack: '' });
        setIsAddingProject(false);
        setSuccessMessage('Project added successfully!');
      } else {
        setError(response.error || 'Failed to add project');
      }
    } catch (err) {
      setError('An error occurred while adding project');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRemoveProject = async (projectId) => {
    setIsLoading(true);
    setError('');
    setSuccessMessage('');
    
    try {
      const response = await StudentService.removeProject(projectId);
      
      if (response.success) {
        setProjects(projects.filter(project => project.id !== projectId));
        setSuccessMessage('Project removed successfully!');
      } else {
        setError(response.error || 'Failed to remove project');
      }
    } catch (err) {
      setError('An error occurred while removing project');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading && !student) {
    return (
      <div className="h-full flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary-500"></div>
      </div>
    );
  }

  return (
    <div className="h-full">
      {/* Error and Success Messages */}
      {error && (
        <div className="mb-4 bg-red-50 border-l-4 border-red-500 p-4 rounded">
          <p className="text-sm text-red-700">{error}</p>
        </div>
      )}
      {successMessage && (
        <div className="mb-4 bg-green-50 border-l-4 border-green-500 p-4 rounded">
          <p className="text-sm text-green-700">{successMessage}</p>
        </div>
      )}

      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-800">My Profile</h1>
        <p className="text-gray-600">View and update your information</p>
      </div>

      {/* Personal Information Section */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold">Personal Information</h2>
          <button 
            onClick={handleEditToggle}
            className={`px-4 py-2 rounded-md ${
              isEditing 
                ? 'bg-gray-500 text-white' 
                : 'bg-primary-600 text-white'
            }`}
          >
            {isEditing ? 'Cancel' : 'Edit Profile'}
          </button>
        </div>

        {isEditing ? (
          // Edit mode
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Name</label>
                <input
                  type="text"
                  name="name"
                  value={editData.name}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Email</label>
                <input
                  type="email"
                  name="email"
                  value={editData.email}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Department</label>
                <input
                  type="text"
                  name="department"
                  value={editData.department}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Year</label>
                <input
                  type="number"
                  name="year"
                  value={editData.year}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">CGPA</label>
                <input
                  type="number"
                  step="0.01"
                  name="cgpa"
                  value={editData.cgpa}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Problems Solved</label>
                <input
                  type="number"
                  name="problemSolvedCount"
                  value={editData.problemSolvedCount}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">GitHub Link</label>
                <input
                  type="url"
                  name="githubLink"
                  value={editData.githubLink}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  placeholder="https://github.com/username"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Portfolio Link</label>
                <input
                  type="url"
                  name="portfolioLink"
                  value={editData.portfolioLink}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  placeholder="https://your-portfolio.com"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Resume Link</label>
                <input
                  type="url"
                  name="resumeLink"
                  value={editData.resumeLink}
                  onChange={handleInputChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  placeholder="https://drive.google.com/resume"
                />
              </div>
            </div>
            <div className="flex justify-end">
              <button
                onClick={handleProfileUpdate}
                className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              >
                Save Changes
              </button>
            </div>
          </div>
        ) : (
          // View mode
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <p className="text-sm text-gray-600">Name</p>
                <p className="font-medium">{student.name}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Email</p>
                <p className="font-medium">{student.email}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Department</p>
                <p className="font-medium">{student.department}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Year</p>
                <p className="font-medium">{student.year}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">CGPA</p>
                <p className="font-medium">{student.cgpa}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Problems Solved</p>
                <p className="font-medium">{student.problemSolvedCount}</p>
              </div>
            </div>

            <div className="border-t pt-4">
              <h3 className="font-semibold mb-3">Links</h3>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <a
                  href={student.githubLink}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center text-primary-600 hover:text-primary-700"
                >
                  <FaGithub className="mr-2" /> GitHub
                </a>
                <a
                  href={student.portfolioLink}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center text-primary-600 hover:text-primary-700"
                >
                  <FaGlobe className="mr-2" /> Portfolio
                </a>
                <a
                  href={student.resumeLink}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center text-primary-600 hover:text-primary-700"
                >
                  <FaFileAlt className="mr-2" /> Resume
                </a>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Skills Section */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold">Skills</h2>
          {!isAddingSkill && (
            <button
              onClick={() => setIsAddingSkill(true)}
              className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
            >
              <FaPlus className="mr-2" /> Add Skill
            </button>
          )}
        </div>

        {isAddingSkill && (
          <div className="mb-6 bg-gray-50 p-4 rounded-md">
            <h3 className="font-semibold mb-3">Add New Skill</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Skill Name</label>
                <input
                  type="text"
                  name="name"
                  value={newSkill.name}
                  onChange={handleNewSkillChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  placeholder="E.g. JavaScript, Python, AWS"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Skill Level</label>
                <select
                  name="level"
                  value={newSkill.level}
                  onChange={handleNewSkillChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                >
                  <option value="BEGINNER">Beginner</option>
                  <option value="INTERMEDIATE">Intermediate</option>
                  <option value="ADVANCED">Advanced</option>
                </select>
              </div>
            </div>
            <div className="flex justify-end space-x-3">
              <button 
                onClick={() => setIsAddingSkill(false)}
                className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
              >
                Cancel
              </button>
              <button 
                onClick={handleAddSkill}
                className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
              >
                Save Skill
              </button>
            </div>
          </div>
        )}

        {skills.length === 0 ? (
          <p className="text-gray-500">No skills added yet.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {skills.map(skill => (
              <div key={skill.id} className="bg-gray-50 rounded-md p-4 flex justify-between items-start">
                <div>
                  <h3 className="font-medium">{skill.name}</h3>
                  <span className={`inline-block mt-1 px-2 py-1 text-xs rounded-full font-medium
                    ${skill.level === 'BEGINNER' ? 'bg-blue-100 text-blue-800' : 
                      skill.level === 'INTERMEDIATE' ? 'bg-green-100 text-green-800' : 
                      'bg-purple-100 text-purple-800'}`}
                  >
                    {skill.level}
                  </span>
                </div>
                <button 
                  onClick={() => handleRemoveSkill(skill.id)}
                  className="text-red-500 hover:text-red-700"
                >
                  <FaTimes />
                </button>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Projects Section */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold">Projects</h2>
          {!isAddingProject && (
            <button
              onClick={() => setIsAddingProject(true)}
              className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
            >
              <FaPlus className="mr-2" /> Add Project
            </button>
          )}
        </div>

        {isAddingProject && (
          <div className="mb-6 bg-gray-50 p-4 rounded-md">
            <h3 className="font-semibold mb-3">Add New Project</h3>
            <div className="space-y-4 mb-4">
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Project Title</label>
                <input
                  type="text"
                  name="title"
                  value={newProject.title}
                  onChange={handleNewProjectChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  placeholder="E.g. E-commerce Website, Chat Application"
                />
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Description</label>
                <textarea
                  name="description"
                  value={newProject.description}
                  onChange={handleNewProjectChange}
                  rows="3"
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  placeholder="Briefly describe your project"
                ></textarea>
              </div>
              <div>
                <label className="block text-gray-700 text-sm font-bold mb-2">Tech Stack</label>
                <input
                  type="text"
                  name="techStack"
                  value={newProject.techStack}
                  onChange={handleNewProjectChange}
                  className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                  placeholder="E.g. React, Node.js, MongoDB"
                />
              </div>
            </div>
            <div className="flex justify-end space-x-3">
              <button 
                onClick={() => setIsAddingProject(false)}
                className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
              >
                Cancel
              </button>
              <button 
                onClick={handleAddProject}
                className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
              >
                Save Project
              </button>
            </div>
          </div>
        )}

        {projects.length === 0 ? (
          <p className="text-gray-500">No projects added yet.</p>
        ) : (
          <div className="space-y-6">
            {projects.map(project => (
              <div key={project.id} className="border rounded-md p-4">
                <div className="flex justify-between">
                  <h3 className="font-semibold text-lg">{project.title}</h3>
                  <button 
                    onClick={() => handleRemoveProject(project.id)}
                    className="text-red-500 hover:text-red-700"
                  >
                    <FaTimes />
                  </button>
                </div>
                <p className="text-gray-600 my-2">{project.description}</p>
                <div className="mt-3 pt-3 border-t border-gray-100">
                  <span className="text-sm font-medium">Tech Stack: </span>
                  <span className="text-sm text-gray-600">{project.techStack}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default StudentProfile;