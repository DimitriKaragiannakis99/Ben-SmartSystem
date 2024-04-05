import React, { useState,useContext } from 'react';
import { OutputConsoleContext } from "../OutputConsoleProvider"
import { CurrentUserContext } from "../CurrentUserProvider";

const ScheduleTemperatureModal = ({ isOpen, onClose, zoneId }) => {
  const [selectedTimes, setSelectedTimes] = useState([]);
  const [temperature, setTemperature] = useState('');

    	  // added OutputConsoleContext
const {consoleMessages, updateConsoleMessages} = useContext(OutputConsoleContext);

const {currSimUser, updateCurrentSimUser} = useContext(CurrentUserContext);

  const handleTimeSelection = (time) => {
    setSelectedTimes((prev) =>
      prev.includes(time) ? prev.filter(t => t !== time) : [...prev, time]
    );
  };

  const handleSubmit = async () => {
    const cleanedZoneId = typeof zoneId === 'string' ?zoneId.replace(/\D/g, "") : zoneId;
    const endpoint = `/api/zones/${cleanedZoneId}/scheduleTemperature`;
    try {
      // Make an asynchronous POST request to the backend
      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          timeOfDay: selectedTimes,
          temperature: parseFloat(temperature),
        }),
      });

      const currentDate = new Date().toLocaleDateString();
      const currentTime = new Date().toLocaleTimeString();
      const message = `[${currentDate}][${currentTime}] the desired temperature ${temperature}C in ${selectedTimes}  has been changed in zone id ${cleanedZoneId} by ${currSimUser}`;
      // updating OutputConsole context
      updateConsoleMessages(message);

      if (!response.ok) {
        // Handle HTTP errors
        throw new Error('Failed to schedule temperature. Please try again.');
      }

      const data = await response.json();

      onClose(); // Close the modal upon successful submission
    } catch (error) {
      console.error('Error:', error);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white p-5 rounded-lg shadow-lg w-1/3">
        <h2 className="text-lg font-semibold mb-4">Schedule Temperature</h2>
        <div className="flex flex-wrap gap-2 mb-4">
          {["morning", "afternoon", "evening"].map((time) => (
            <label key={time} className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={selectedTimes.includes(time)}
                onChange={() => handleTimeSelection(time)}
                className="form-checkbox"
              />
              {time.charAt(0).toUpperCase() + time.slice(1)}
            </label>
          ))}
        </div>
        <div className="mb-4">
          <label htmlFor="desiredTemp" className="block mb-2">Desired Temperature:</label>
          <input
            type="number"
            id="desiredTemp"
            value={temperature}
            onChange={(e) => setTemperature(e.target.value)}
            className="border rounded px-2 py-1"
            min="0"
            max="100"
          />
        </div>
        <button
          onClick={handleSubmit}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Update
        </button>
        <button
          onClick={onClose}
          className="ml-2 bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
        >
          Cancel
        </button>
      </div>
    </div>
  );
};

export default ScheduleTemperatureModal;
