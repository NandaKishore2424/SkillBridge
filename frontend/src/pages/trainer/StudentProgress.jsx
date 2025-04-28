import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import AuthService from '../../services/authService';

const StudentProgress = () => {
  const { batchId, studentId } = useParams();
  const [student, setStudent] = useState(null);
  const [topics, setTopics] = useState([]);
  const [progress, setProgress] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [feedback, setFeedback] = useState('');
  const [selectedTopic, setSelectedTopic] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Get student details
        const studentResponse = await axios.get(`/api/v1/students/${studentId}`);
        setStudent(studentResponse.data);

        // Get batch syllabus topics
        const topicsResponse = await axios.get(`/api/v1/batches/${batchId}/syllabus/topics`);
        setTopics(topicsResponse.data);

        // Get current progress
        const progressResponse = await axios.get(`/api/v1/progress/student/${studentId}/batch/${batchId}`);
        setProgress(progressResponse.data);
        
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };

    fetchData();
  }, [studentId, batchId]);

  const getTopicProgress = (topicId) => {
    return progress.find(p => p.topic.id === topicId) || { status: 'PENDING', feedback: '' };
  };

  const handleStatusChange = (topicId, status) => {
    setSelectedTopic(topicId);
    setSelectedStatus(status);
    
    // Find existing feedback if any
    const existingProgress = progress.find(p => p.topic.id === topicId);
    setFeedback(existingProgress ? existingProgress.feedback : '');
  };

  const handleSaveProgress = async () => {
    if (!selectedTopic || !selectedStatus) return;
    
    setIsSaving(true);
    try {
      const response = await axios.post('/api/v1/progress/update', {
        studentId,
        batchId,
        topicId: selectedTopic,
        status: selectedStatus,
        feedback
      });
      
      // Update local state with new progress
      setProgress(prev => {
        const newProgress = [...prev];
        const index = newProgress.findIndex(p => p.topic.id === selectedTopic);
        
        if (index >= 0) {
          newProgress[index] = response.data;
        } else {
          newProgress.push(response.data);
        }
        
        return newProgress;
      });
      
      // Reset form
      setSelectedTopic(null);
      setSelectedStatus('');
      setFeedback('');
    } catch (error) {
      console.error("Error updating progress", error);
    } finally {
      setIsSaving(false);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'COMPLETED': return 'bg-green-100 text-green-800';
      case 'IN_PROGRESS': return 'bg-blue-100 text-blue-800';
      case 'NEEDS_IMPROVEMENT': return 'bg-yellow-100 text-yellow-800';
      default: return 'bg-gray-100 text-gray-800';
    }
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
        <h1 className="text-2xl font-bold text-gray-800">Student Progress</h1>
        <p className="text-gray-600">Track progress for {student.name}</p>
      </div>

      {/* Progress Table */}
      <div className="bg-white rounded-lg shadow overflow-hidden mb-6">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Topic</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Last Updated</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {topics.map((topic) => {
              const topicProgress = getTopicProgress(topic.id);
              return (
                <tr key={topic.id}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">{topic.name}</div>
                    <div className="text-sm text-gray-500">{topic.description}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusClass(topicProgress.status)}`}>
                      {topicProgress.status?.replace('_', ' ')}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {topicProgress.updatedAt ? new Date(topicProgress.updatedAt).toLocaleDateString() : 'Not updated yet'}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button
                      onClick={() => handleStatusChange(topic.id, topicProgress.status || 'PENDING')}
                      className="text-primary-600 hover:text-primary-900"
                    >
                      Update
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {/* Update Progress Form */}
      {selectedTopic && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold mb-4">Update Progress</h2>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Topic
            </label>
            <div className="text-sm text-gray-700">
              {topics.find(t => t.id === selectedTopic)?.name}
            </div>
          </div>
          
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Status
            </label>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            >
              <option value="PENDING">Pending</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="COMPLETED">Completed</option>
              <option value="NEEDS_IMPROVEMENT">Needs Improvement</option>
            </select>
          </div>
          
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">
              Feedback
            </label>
            <textarea
              value={feedback}
              onChange={(e) => setFeedback(e.target.value)}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              rows="4"
              placeholder="Provide feedback to the student..."
            ></textarea>
          </div>
          
          <div className="flex justify-end">
            <button
              onClick={() => {
                setSelectedTopic(null);
                setSelectedStatus('');
                setFeedback('');
              }}
              className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded mr-2"
              disabled={isSaving}
            >
              Cancel
            </button>
            <button
              onClick={handleSaveProgress}
              className="bg-primary-600 hover:bg-primary-700 text-white font-bold py-2 px-4 rounded"
              disabled={isSaving}
            >
              {isSaving ? 'Saving...' : 'Save Progress'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default StudentProgress;