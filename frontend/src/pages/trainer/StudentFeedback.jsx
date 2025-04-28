import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import AuthService from '../../services/authService';
import { FaStar, FaRegStar } from 'react-icons/fa';

const StudentFeedback = () => {
  const { studentId, batchId } = useParams();
  const [student, setStudent] = useState(null);
  const [feedback, setFeedback] = useState('');
  const [rating, setRating] = useState(0);
  const [previousFeedbacks, setPreviousFeedbacks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSending, setIsSending] = useState(false);
  const navigate = useNavigate();
  const user = AuthService.getCurrentUser();

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Get student details
        const studentResponse = await axios.get(`/api/v1/students/${studentId}`);
        setStudent(studentResponse.data);

        // Get previous feedback
        const feedbackResponse = await axios.get(`/api/v1/feedback/trainer/for-student/${studentId}`);
        setPreviousFeedbacks(feedbackResponse.data.filter(f => f.batch.id === batchId));
        
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };

    fetchData();
  }, [studentId, batchId]);

  const handleRatingClick = (value) => {
    setRating(value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (rating === 0) {
      alert("Please select a rating");
      return;
    }
    
    if (!feedback.trim()) {
      alert("Please provide feedback");
      return;
    }
    
    setIsSending(true);
    try {
      await axios.post(`/api/v1/feedback/trainer/${user.id}`, {
        studentId,
        batchId,
        content: feedback,
        rating
      });
      
      // Redirect back to student list
      navigate(`/trainer/batches/${batchId}/students`);
    } catch (error) {
      console.error("Error submitting feedback", error);
      alert("Failed to submit feedback. Please try again.");
    } finally {
      setIsSending(false);
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString();
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
        <h1 className="text-2xl font-bold text-gray-800">Provide Feedback</h1>
        <p className="text-gray-600">Give feedback to {student.name}</p>
      </div>

      {/* Student Info */}
      <div className="bg-white rounded-lg shadow p-6 mb-8">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p className="text-sm text-gray-500">Name</p>
            <p className="font-medium">{student.name}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Email</p>
            <p className="font-medium">{student.email}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Department</p>
            <p className="font-medium">{student.department}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Year</p>
            <p className="font-medium">{student.year}</p>
          </div>
        </div>
      </div>

      {/* Feedback Form */}
      <div className="bg-white rounded-lg shadow p-6 mb-8">
        <h2 className="text-lg font-semibold mb-4">New Feedback</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">Rating</label>
            <div className="flex space-x-2">
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  key={star}
                  type="button"
                  onClick={() => handleRatingClick(star)}
                  className="focus:outline-none text-2xl"
                >
                  {star <= rating ? (
                    <FaStar className="text-yellow-400" />
                  ) : (
                    <FaRegStar className="text-gray-300" />
                  )}
                </button>
              ))}
              <span className="ml-2 text-gray-600">{rating > 0 ? `${rating}/5` : ''}</span>
            </div>
          </div>
          
          <div className="mb-6">
            <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="feedback">
              Feedback
            </label>
            <textarea
              id="feedback"
              value={feedback}
              onChange={(e) => setFeedback(e.target.value)}
              rows="4"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="Provide detailed feedback about this student's performance..."
            ></textarea>
          </div>
          
          <div className="flex justify-end space-x-3">
            <button
              type="button"
              onClick={() => navigate(`/trainer/batches/${batchId}/students`)}
              className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
              disabled={isSending}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700"
              disabled={isSending}
            >
              {isSending ? 'Sending...' : 'Submit Feedback'}
            </button>
          </div>
        </form>
      </div>

      {/* Previous Feedback */}
      {previousFeedbacks.length > 0 && (
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold mb-4">Previous Feedback</h2>
          <div className="space-y-6">
            {previousFeedbacks.map((item) => (
              <div key={item.id} className="border-b border-gray-200 pb-4 last:border-0 last:pb-0">
                <div className="flex items-center mb-2">
                  <div className="flex">
                    {[...Array(5)].map((_, i) => (
                      <span key={i} className="text-lg">
                        {i < item.rating ? (
                          <FaStar className="text-yellow-400" />
                        ) : (
                          <FaRegStar className="text-gray-300" />
                        )}
                      </span>
                    ))}
                  </div>
                  <span className="ml-2 text-sm text-gray-500">
                    {formatDate(item.timestamp)}
                  </span>
                </div>
                <p className="text-gray-700">{item.content}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default StudentFeedback;