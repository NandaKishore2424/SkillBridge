import React, { useState, useEffect } from 'react';
import { FaUsers, FaLaptopCode, FaChalkboardTeacher, FaBuilding, FaArrowUp, FaArrowDown } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';

const AdminDashboard = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    students: { count: 0, trend: 0 },
    batches: { count: 0, trend: 0 },
    trainers: { count: 0, trend: 0 },
    companies: { count: 0, trend: 0 }
  });

  const [recentActivities, setRecentActivities] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => {
      setStats({
        students: { count: 156, trend: 12 },
        batches: { count: 8, trend: 2 },
        trainers: { count: 14, trend: -1 },
        companies: { count: 32, trend: 5 }
      });
      
      setRecentActivities([
        { id: 1, action: 'New student registered', user: 'Rahul Sharma', timestamp: '2 hours ago' },
        { id: 2, action: 'Batch assigned', user: 'Priya Singh', timestamp: '3 hours ago' },
        { id: 3, action: 'New company added', user: 'Admin', timestamp: '5 hours ago' },
        { id: 4, action: 'Trainer assigned to batch', user: 'Admin', timestamp: '1 day ago' },
        { id: 5, action: 'Student completed batch', user: 'Aakash Patel', timestamp: '1 day ago' },
      ]);
      
      setIsLoading(false);
    }, 1000);
  }, []);

  // Summary cards for key metrics
  const StatCard = ({ title, count, icon, trend }) => (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-gray-500 text-sm font-medium uppercase">{title}</p>
          <p className="text-3xl font-bold mt-2 text-gray-800">{count}</p>
        </div>
        <div className={`p-3 rounded-full ${title === 'Students' ? 'bg-blue-100 text-blue-800' : 
                                          title === 'Batches' ? 'bg-green-100 text-green-800' : 
                                          title === 'Trainers' ? 'bg-purple-100 text-purple-800' : 
                                          'bg-orange-100 text-orange-800'}`}>
          {icon}
        </div>
      </div>
      <div className="mt-4 flex items-center">
        {trend > 0 ? (
          <span className="text-green-600 flex items-center text-sm">
            <FaArrowUp className="mr-1" /> {trend}% <span className="ml-1 text-gray-500">since last month</span>
          </span>
        ) : (
          <span className="text-red-600 flex items-center text-sm">
            <FaArrowDown className="mr-1" /> {Math.abs(trend)}% <span className="ml-1 text-gray-500">since last month</span>
          </span>
        )}
      </div>
    </div>
  );

  // Updated Quick Action component with navigation
  const QuickAction = ({ title, icon, bgColor, path }) => (
    <button 
      onClick={() => navigate(path)}
      className={`${bgColor} text-white p-4 rounded-lg flex items-center justify-center flex-col shadow-md transition-transform hover:scale-105`}
    >
      <span className="text-2xl mb-2">{icon}</span>
      <span className="text-sm font-medium">{title}</span>
    </button>
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
        <h1 className="text-2xl font-bold text-gray-800">Admin Dashboard</h1>
        <p className="text-gray-600">Welcome back, Admin! Here's what's happening today.</p>
      </div>

      {/* Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard 
          title="Students" 
          count={stats.students.count} 
          icon={<FaUsers size={24} />} 
          trend={stats.students.trend} 
        />
        <StatCard 
          title="Batches" 
          count={stats.batches.count} 
          icon={<FaLaptopCode size={24} />} 
          trend={stats.batches.trend} 
        />
        <StatCard 
          title="Trainers" 
          count={stats.trainers.count} 
          icon={<FaChalkboardTeacher size={24} />} 
          trend={stats.trainers.trend} 
        />
        <StatCard 
          title="Companies" 
          count={stats.companies.count} 
          icon={<FaBuilding size={24} />} 
          trend={stats.companies.trend} 
        />
      </div>

      {/* Quick Actions */}
      <div className="mb-8">
        <h2 className="text-lg font-semibold text-gray-800 mb-4">Quick Actions</h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <QuickAction 
            title="Add Student" 
            icon={<FaUsers />} 
            bgColor="bg-blue-600 hover:bg-blue-700"
            path="/admin/students/add"
          />
          <QuickAction 
            title="Create Batch" 
            icon={<FaLaptopCode />} 
            bgColor="bg-green-600 hover:bg-green-700"
            path="/admin/batches/add"
          />
          <QuickAction 
            title="Add Trainer" 
            icon={<FaChalkboardTeacher />} 
            bgColor="bg-purple-600 hover:bg-purple-700"
            path="/admin/trainers/add" 
          />
          <QuickAction 
            title="Add Company" 
            icon={<FaBuilding />} 
            bgColor="bg-orange-600 hover:bg-orange-700"
            path="/admin/companies/add"
          />
        </div>
      </div>

      {/* Recent Activities */}
      <div>
        <h2 className="text-lg font-semibold text-gray-800 mb-4">Recent Activities</h2>
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Time</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {recentActivities.map((activity) => (
                <tr key={activity.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{activity.action}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{activity.user}</td>
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

export default AdminDashboard;