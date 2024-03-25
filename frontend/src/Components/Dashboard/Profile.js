import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import UserSelector from "./UserSelector";
import DateTimeDisplay from "./DateTimeDisplay";
import SimulationToggle from "./SimulationToggle";
import EditSimulation from "../EditSimulationComponent";
import axios from "axios";

const Profile = () => {
  const [showSimulation, setShowSimulation] = useState(false);
  const [userLocation, setUserLocation] = useState("Unknown");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserLocation = async () => {
      const currentUserId = localStorage.getItem("currentUserId");
      if (currentUserId) {
        try {
          const response = await axios.get(
            `http://localhost:8080/api/users/${currentUserId}`
          );
          // Assuming the user's location is stored in a property called 'location'
          setUserLocation(response.data.location || "Unknowwern");
        } catch (error) {
          console.error("Error fetching user location:", error);
        }
      }
    };

    fetchUserLocation();
    const intervalId = setInterval(fetchUserLocation, 5000); // Update location every 5 seconds

    return () => clearInterval(intervalId); // Clean up the interval on component unmount
  }, []);

  const handleClick = () => {
    setShowSimulation(true);
    navigate("/edit-rooms");
  };

  const handleUploadTemperatures = () => {
    navigate("/uploadTemps");
  };

  return (
    <div className="flex flex-col items-center h-full">
      <h1 className="text-3xl font-bold underline mb-20">Simulation</h1>
      <div className="mb-16">
        <UserSelector />
      </div>
      <button
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded mb-16"
        onClick={handleClick}
      >
        Edit Simulation
      </button>
      <button
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded mb-16"
        onClick={handleUploadTemperatures}
      >
        Upload Temperatures
      </button>
      {showSimulation && <EditSimulation />}
      <div className="mb-16">
        <SimulationToggle />
      </div>
      <div className="mb-16">
        <p>
          Outside Temperature: <strong>25</strong>
        </p>
      </div>
      <div className="mb-16">
        <p>
          Location: <strong>{userLocation}</strong>
        </p>
      </div>
      <div className="mb-16">
        <DateTimeDisplay />
      </div>
    </div>
  );
};

export default Profile;
