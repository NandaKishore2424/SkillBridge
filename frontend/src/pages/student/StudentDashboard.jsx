import React, { useState, useEffect } from 'react';
import { FaLaptopCode, FaCode, FaUserGraduate, FaBuilding, FaCalendarAlt } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import AuthService from '../../services/authService';
import axios from 'axios';

const StudentDashboard = () => {
  const [studentData, setStudentData] = useState(null);
  const [currentBatch, setCurrentBatch] = useState(null);
  const [skills, setSkills] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [recommendedBatches, setRecommendedBatches] = useState([]);
  const user = AuthService.getCurrentUser();

  useEffect(() => {
    
    setTimeout(() => {
      setStudentData({
        name: "Test Student",
        department: "CSE",
        year: 3,
        cgpa: 8.5,
        problemsSolved: 120
      });
      
      setCurrentBatch({
        name: "Full Stack Batch 2025",
        startDate: "Jan 15, 2025",
        endDate: "Apr 15, 2025",
        progress: 65, // percentage
        trainer: "John Smith"
      });
      
      setSkills([
        { name: "Java", level: "INTERMEDIATE", projects: 3 },
        { name: "React", level: "BEGINNER", projects: 1 },
        { name: "SQL", level: "ADVANCED", projects: 5 },
        { name: "DSA", level: "INTERMEDIATE", projects: 0 }
      ]);
      
      setIsLoading(false);
    }, 1000);
  }, []);

  useEffect(() => {
    // Fetch recommended batches
    const fetchRecommendations = async () => {
      try {
        const user = AuthService.getCurrentUser();
        const response = await axios.get(`/api/v1/students/${user.id}/recommend-batches`);
        setRecommendedBatches(response.data);
      } catch (error) {
        console.error("Failed to fetch batch recommendations", error);
      }
    };
    
    fetchRecommendations();
  }, []);

  // Card component for displaying metrics
  const StatCard = ({ title, value, icon, bgColor }) => (
    <div className="bg-white rounded-lg shadow p-5 flex items-center space-x-4">
      <div className={`p-3 rounded-full ${bgColor}`}>
        {icon}
      </div>
      <div>
        <p className="text-gray-500 text-sm font-medium">{title}</p>
        <p className="text-xl font-bold text-gray-800">{value}</p>
      </div>
    </div>
  );

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-full">
        <div className="animate-spin rounded-full h-16 w-16 border-t-2 border-b-2 border-primary-600"></div>
      </div>
    );
  }
  
  return (
    <div className="h-full">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Student Dashboard</h1>
        <p className="text-gray-600">Welcome back, {studentData.name}!</p>
      </div>

      {/* Student Stats */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard 
          title="Department" 
          value={studentData.department} 
          icon={<FaUserGraduate className="text-blue-600" size={20} />}
          bgColor="bg-blue-100" 
        />
        <StatCard 
          title="Year" 
          value={studentData.year} 
          icon={<FaCalendarAlt className="text-green-600" size={20} />} 
          bgColor="bg-green-100" 
        />
        <StatCard 
          title="CGPA" 
          value={studentData.cgpa} 
          icon={<FaCode className="text-purple-600" size={20} />} 
          bgColor="bg-purple-100" 
        />
        <StatCard 
          title="Problems Solved" 
          value={studentData.problemsSolved} 
          icon={<FaCode className="text-orange-600" size={20} />}
          bgColor="bg-orange-100"  
        />
      </div>

      {/* Current Batch */}
      <div className="mb-8">
        <h2 className="text-lg font-semibold text-gray-800 mb-4">Current Batch</h2>
        {currentBatch ? (
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex justify-between items-center mb-4">
              <div>
                <h3 className="font-bold text-lg">{currentBatch.name}</h3>
                <p className="text-sm text-gray-500">
                  {currentBatch.startDate} - {currentBatch.endDate}
                </p>
              </div>
              <Link 
                to="/student/batch" 
                className="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 transition-colors"
              >
                View Details
              </Link>
            </div>

            <div className="mb-2 flex justify-between">
              <span className="text-sm font-medium">Progress</span>
              <span className="text-sm font-medium">{currentBatch.progress}%</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2.5">
              <div className="bg-primary-600 h-2.5 rounded-full" style={{ width: `${currentBatch.progress}%` }}></div>
            </div>
            
            <p className="mt-4 text-sm text-gray-600">
              <span className="font-medium">Trainer:</span> {currentBatch.trainer}
            </p>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow p-6 text-center">
            <p className="text-gray-500">You are not currently enrolled in any batch.</p>
            <Link 
              to="/student/apply" 
              className="mt-4 inline-block px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 transition-colors"
            >
              Apply for a Batch
            </Link>
          </div>
        )}
      </div>

      {/* Recommended Batches */}
      {recommendedBatches.length > 0 && (
        <div className="mb-8">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">Recommended Batches</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {recommendedBatches.slice(0, 3).map((batch) => (
              <div key={batch.batchId} className="bg-white rounded-lg shadow p-6 border-l-4 border-primary-500">
                <div className="flex justify-between mb-2">
                  <h3 className="font-bold text-lg">{batch.batchName}</h3>
                  <span className="text-sm bg-green-100 text-green-800 px-2 py-1 rounded-full">
                    {batch.totalScore} match points
                  </span>
                </div>
                
                <p className="text-sm text-gray-600 mb-4">{batch.description}</p>
                
                <div className="mb-4">
                  <p className="text-sm"><span className="font-medium">Duration:</span> {batch.durationWeeks} weeks</p>
                  <p className="text-sm"><span className="font-medium">Trainer:</span> {batch.trainerName}</p>
                  <p className="text-sm"><span className="font-medium">Starts:</span> {batch.startDate}</p>
                </div>
                
                <div className="mb-4 space-y-2">
                  <h4 className="text-sm font-semibold">Why this batch is a good fit:</h4>
                  <ul className="list-disc list-inside text-sm text-gray-600">
                    {batch.matchReasons.map((reason, index) => (
                      <li key={index}>{reason}</li>
                    ))}
                  </ul>
                </div>
                
                <div className="flex space-x-3 mt-4">
                  <button className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-md text-sm">
                    Apply Now
                  </button>
                  <button className="border border-gray-300 text-gray-600 hover:bg-gray-50 px-4 py-2 rounded-md text-sm">
                    View Details
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Skills Overview */}
      <div className="mb-8">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-semibold text-gray-800">My Skills</h2>
          <Link 
            to="/student/profile" 
            className="text-primary-600 hover:text-primary-700 text-sm font-medium"
          >
            Manage Skills
          </Link>
        </div>
        
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Skill</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Level</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Projects</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {skills.map((skill, index) => (
                <tr key={index}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{skill.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <span className={`px-2 py-1 rounded-full text-xs font-medium
                      ${skill.level === 'BEGINNER' ? 'bg-blue-100 text-blue-800' : 
                        skill.level === 'INTERMEDIATE' ? 'bg-green-100 text-green-800' : 
                        'bg-purple-100 text-purple-800'}`}
                    >
                      {skill.level}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{skill.projects}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default StudentDashboard;