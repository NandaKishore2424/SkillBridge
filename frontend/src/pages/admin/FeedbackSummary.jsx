import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { FaStar, FaRegStar, FaChalkboardTeacher, FaUserGraduate } from 'react-icons/fa';

const FeedbackSummary = () => {
  const [topTrainers, setTopTrainers] = useState([]);
  const [batchSummaries, setBatchSummaries] = useState([]);
  const [batches, setBatches] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Get top rated trainers
        const trainersResponse = await axios.get('/api/v1/feedback/top-trainers?limit=5');
        setTopTrainers(trainersResponse.data);

        // Get all batches
        const batchesResponse = await axios.get('/api/v1/batches');
        setBatches(batchesResponse.data);
        
        // Get feedback summaries for each batch
        const summaries = await Promise.all(
          batchesResponse.data.map(async (batch) => {
            const summaryResponse = await axios.get(`/api/v1/feedback/summary/batch/${batch.id}`);
            return {
              ...summaryResponse.data,
              batchName: batch.name
            };
          })
        );
        
        setBatchSummaries(summaries);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching feedback data", error);
      }
    };

    fetchData();
  }, []);

  // A helper component to render star ratings
  const StarRating = ({ rating }) => {
    return (
      <div className="flex">
        {[...Array(5)].map((_, i) => (
          <span key={i} className="text-lg">
            {i < Math.round(rating) ? (
              <FaStar className="text-yellow-400" />
            ) : (
              <FaRegStar className="text-gray-300" />
            )}
          </span>
        ))}
        <span className="ml-2 text-gray-600">{rating.toFixed(1)}</span>
      </div>
    );
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
        <h1 className="text-2xl font-bold text-gray-800">Feedback Summary</h1>
        <p className="text-gray-600">Monitor feedback across batches and trainers</p>
      </div>

      {/* Top Rated Trainers */}
      <div className="bg-white rounded-lg shadow p-6 mb-8">
        <h2 className="text-lg font-semibold mb-4">Top Rated Trainers</h2>
        {topTrainers.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rank</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Specialization</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rating</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Feedback Count</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {topTrainers.map((trainer, index) => (
                  <tr key={trainer.id}>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="font-bold">{index + 1}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="p-2 rounded-full bg-purple-100 text-purple-600 mr-3">
                          <FaChalkboardTeacher />
                        </div>
                        <div className="text-sm font-medium text-gray-900">{trainer.name}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {trainer.specialization}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <StarRating rating={trainer.averageRating} />
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {trainer.feedbackCount}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-gray-500">No trainer ratings available yet.</p>
        )}
      </div>

      {/* Batch Feedback Summaries */}
      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-lg font-semibold mb-4">Batch Feedback Summaries</h2>
        {batchSummaries.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Batch Name</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Trainer Rating</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Student Rating</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Student Feedback</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Trainer Feedback</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {batchSummaries.map((summary) => (
                  <tr key={summary.batchId}>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-gray-900">{summary.batchName}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {summary.averageTrainerRating > 0 ? (
                        <StarRating rating={summary.averageTrainerRating} />
                      ) : (
                        <span className="text-gray-500">No ratings</span>
                      )}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {summary.averageStudentRating > 0 ? (
                        <StarRating rating={summary.averageStudentRating} />
                      ) : (
                        <span className="text-gray-500">No ratings</span>
                      )}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {summary.studentFeedbackCount} submissions
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {summary.trainerFeedbackCount} submissions
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <Link 
                        to={`/admin/batches/${summary.batchId}/feedback`} 
                        className="text-primary-600 hover:text-primary-900"
                      >
                        View Details
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-gray-500">No batch feedback data available yet.</p>
        )}
      </div>
    </div>
  );
};

export default FeedbackSummary;