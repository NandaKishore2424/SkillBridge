import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import AuthService from '../../services/authService';
import { FaCheckCircle, FaCircle, FaExclamationCircle, FaSpinner } from 'react-icons/fa';

const BatchProgress = () => {
  const { batchId } = useParams();
  const [batchInfo, setBatchInfo] = useState(null);
  const [progress, setProgress] = useState([]);
  const [topics, setTopics] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const user = AuthService.getCurrentUser();

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Get batch details
        const batchResponse = await axios.get(`/api/v1/batches/${batchId}`);
        setBatchInfo(batchResponse.data);

        // Get syllabus topics
        const topicsResponse = await axios.get(`/api/v1/batches/${batchId}/syllabus/topics`);
        setTopics(topicsResponse.data);
        
        // Get student's progress
        const progressResponse = await axios.get(`/api/v1/progress/student/${user.id}/batch/${batchId}`);
        setProgress(progressResponse.data);
        
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };

    fetchData();
  }, [batchId, user.id]);

  const getTopicProgress = (topicId) => {
    return progress.find(p => p.topic.id === topicId) || { status: 'PENDING', feedback: '' };
  };

  const getProgressIcon = (status) => {
    switch (status) {
      case 'COMPLETED': 
        return <FaCheckCircle className="text-green-500" size={20} />;
      case 'IN_PROGRESS': 
        return <FaSpinner className="text-blue-500 animate-spin" size={20} />;
      case 'NEEDS_IMPROVEMENT': 
        return <FaExclamationCircle className="text-yellow-500" size={20} />;
      default: 
        return <FaCircle className="text-gray-300" size={20} />;
    }
  };

  const getProgressPercentage = () => {
    if (progress.length === 0 || topics.length === 0) return 0;
    
    const completedCount = progress.filter(p => p.status === 'COMPLETED').length;
    const inProgressCount = progress.filter(p => p.status === 'IN_PROGRESS').length;
    
    return Math.round(((completedCount + (inProgressCount * 0.5)) / topics.length) * 100);
  };

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
        <h1 className="text-2xl font-bold text-gray-800">{batchInfo.name}</h1>
        <p className="text-gray-600">Track your progress in this batch</p>
      </div>

      {/* Progress Overview */}
      <div className="bg-white rounded-lg shadow p-6 mb-8">
        <div className="mb-4">
          <h3 className="text-lg font-medium">Overall Progress</h3>
          <div className="flex items-center mt-2">
            <div className="flex-grow">
              <div className="w-full bg-gray-200 rounded-full h-4">
                <div 
                  className="bg-primary-600 h-4 rounded-full" 
                  style={{ width: `${getProgressPercentage()}%` }}
                ></div>
              </div>
            </div>
            <span className="ml-4 text-lg font-medium">{getProgressPercentage()}%</span>
          </div>
        </div>
        
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-center">
          <div className="bg-gray-50 p-3 rounded">
            <div className="text-lg font-bold">{topics.length}</div>
            <div className="text-sm text-gray-500">Total Topics</div>
          </div>
          <div className="bg-green-50 p-3 rounded">
            <div className="text-lg font-bold text-green-700">
              {progress.filter(p => p.status === 'COMPLETED').length}
            </div>
            <div className="text-sm text-gray-500">Completed</div>
          </div>
          <div className="bg-blue-50 p-3 rounded">
            <div className="text-lg font-bold text-blue-700">
              {progress.filter(p => p.status === 'IN_PROGRESS').length}
            </div>
            <div className="text-sm text-gray-500">In Progress</div>
          </div>
          <div className="bg-yellow-50 p-3 rounded">
            <div className="text-lg font-bold text-yellow-700">
              {progress.filter(p => p.status === 'NEEDS_IMPROVEMENT').length}
            </div>
            <div className="text-sm text-gray-500">Needs Improvement</div>
          </div>
        </div>
      </div>

      {/* Topics Progress List */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="p-6">
          <h3 className="text-lg font-medium mb-4">Topics Progress</h3>
        </div>
        <ul className="divide-y divide-gray-200">
          {topics.map((topic) => {
            const topicProgress = getTopicProgress(topic.id);
            return (
              <li key={topic.id} className="p-6">
                <div className="flex items-start">
                  <div className="flex-shrink-0 mt-1">
                    {getProgressIcon(topicProgress.status)}
                  </div>
                  <div className="ml-4 flex-grow">
                    <div className="flex justify-between">
                      <h4 className="text-lg font-medium text-gray-900">{topic.name}</h4>
                      <span className={`px-2 py-1 text-xs rounded-full font-medium
                        ${topicProgress.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 
                          topicProgress.status === 'IN_PROGRESS' ? 'bg-blue-100 text-blue-800' : 
                          topicProgress.status === 'NEEDS_IMPROVEMENT' ? 'bg-yellow-100 text-yellow-800' :
                          'bg-gray-100 text-gray-800'}`}
                      >
                        {topicProgress.status?.replace('_', ' ')}
                      </span>
                    </div>
                    <p className="mt-1 text-sm text-gray-600">{topic.description}</p>
                    
                    {topicProgress.feedback && (
                      <div className="mt-3 p-3 bg-gray-50 rounded-md border border-gray-200">
                        <p className="text-sm font-medium text-gray-700">Trainer Feedback:</p>
                        <p className="text-sm text-gray-600">{topicProgress.feedback}</p>
                      </div>
                    )}
                    
                    {topicProgress.updatedAt && (
                      <p className="mt-2 text-xs text-gray-500">
                        Last updated: {new Date(topicProgress.updatedAt).toLocaleString()}
                      </p>
                    )}
                  </div>
                </div>
              </li>
            );
          })}
        </ul>
      </div>
    </div>
  );
};

export default BatchProgress;