import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaUsers, FaLaptopCode, FaCalendarAlt, FaFileAlt, FaArrowRight } from 'react-icons/fa';
import AuthService from '../../services/authService';

const TrainerDashboard = () => {
  const [trainerData, setTrainerData] = useState(null);
  const [assignedBatches, setAssignedBatches] = useState([]);
  const [recentActivities, setRecentActivities] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // In a real app, fetch this data from your API
    // For now, we'll use mock data
    setTimeout(() => {
      setTrainerData({
        name: "John Smith",
        specialization: "Full Stack Development",
        batchCount: 2,
        studentCount: 48
      });
      
      setAssignedBatches([
        {
          id: '1',
          name: 'Full Stack Batch 2025',
          students: 25,
          progress: 65,
          startDate: 'Jan 15, 2025',
          endDate: 'Apr 15, 2025'
        },
        {
          id: '2',
          name: 'Web Dev Essentials',
          students: 23,
          progress: 40,
          startDate: 'Feb 10, 2025',
          endDate: 'May 10, 2025'
        }
      ]);
      
      setRecentActivities([
        { id: 1, action: 'Student Rahul Singh submitted an assignment', batch: 'Full Stack Batch 2025', timestamp: '3 hours ago' },
        { id: 2, action: 'You uploaded new resources', batch: 'Web Dev Essentials', timestamp: '1 day ago' },
        { id: 3, action: 'Student Priya Patel completed week 6 quiz', batch: 'Full Stack Batch 2025', timestamp: '2 days ago' },
        { id: 4, action: 'Admin added 2 new students to your batch', batch: 'Web Dev Essentials', timestamp: '3 days ago' }
      ]);
      
      setIsLoading(false);
    }, 1000);
  }, []);

  // Stats card component
  const StatCard = ({ title, value, icon, bgColor }) => (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-gray-500 text-sm font-medium uppercase">{title}</p>
          <p className="text-3xl font-bold mt-2 text-gray-800">{value}</p>
        </div>
        <div className={`p-3 rounded-full ${bgColor}`}>
          {icon}
        </div>
      </div>
    </div>
  );

  // Batch card component
  const BatchCard = ({ batch }) => (
    <div className="bg-white rounded-lg shadow overflow-hidden">
      <div className="p-5">
        <h3 className="text-lg font-semibold">{batch.name}</h3>
        <div className="flex justify-between text-sm text-gray-500 mt-1">
          <span>{batch.startDate} - {batch.endDate}</span>
          <span>{batch.students} students</span>
        </div>
        
        <div className="mt-4">
          <div className="flex justify-between mb-1 text-sm">
            <span>Progress</span>
            <span>{batch.progress}%</span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-2">
            <div 
              className="bg-primary-600 h-2 rounded-full" 
              style={{ width: `${batch.progress}%` }}
            ></div>
          </div>
        </div>
      </div>
      <div className="bg-gray-50 px-5 py-3 border-t border-gray-100">
        <Link 
          to={`/trainer/batches/${batch.id}`}
          className="text-primary-600 hover:text-primary-800 text-sm font-medium flex items-center"
        >
          View details
          <FaArrowRight className="ml-1 text-xs" />
        </Link>
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
        <h1 className="text-2xl font-bold text-gray-800">Trainer Dashboard</h1>
        <p className="text-gray-600">Welcome back, {trainerData.name}!</p>
      </div>

      {/* Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard 
          title="Specialization" 
          value={trainerData.specialization}
          icon={<FaLaptopCode size={24} className="text-blue-600" />}
          bgColor="bg-blue-100"
        />
        <StatCard 
          title="Assigned Batches" 
          value={trainerData.batchCount}
          icon={<FaCalendarAlt size={24} className="text-green-600" />}
          bgColor="bg-green-100"
        />
        <StatCard 
          title="Total Students" 
          value={trainerData.studentCount}
          icon={<FaUsers size={24} className="text-purple-600" />}
          bgColor="bg-purple-100"
        />
        <StatCard 
          title="Resources Shared" 
          value="32"
          icon={<FaFileAlt size={24} className="text-orange-600" />}
          bgColor="bg-orange-100"
        />
      </div>

      {/* My Batches */}
      <div className="mb-8">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-semibold text-gray-800">My Batches</h2>
          <Link 
            to="/trainer/batches" 
            className="text-primary-600 hover:text-primary-700 text-sm font-medium"
          >
            View all
          </Link>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {assignedBatches.map(batch => (
            <BatchCard key={batch.id} batch={batch} />
          ))}
        </div>
      </div>

      {/* Recent Activities */}
      <div>
        <h2 className="text-lg font-semibold text-gray-800 mb-4">Recent Activities</h2>
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Activity</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Batch</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Time</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {recentActivities.map((activity) => (
                <tr key={activity.id}>
                  <td className="px-6 py-4 whitespace-normal text-sm font-medium text-gray-900">{activity.action}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{activity.batch}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{activity.timestamp}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default TrainerDashboard;