import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import UserSelector from './UserSelector';
import DateTimeDisplay from './DateTimeDisplay';
import SimulationToggle from './SimulationToggle';
import EditSimulaiton from '../EditSimulation';

const Profile = () => {
  const [showSimulation, setShowSimulation] = useState(false);
  const navigate = useNavigate();

  const handleClick = () => {
    setShowSimulation(true);
    navigate('/edit-rooms');
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
      {showSimulation && <EditSimulaiton />}
      <div className="mb-16">
        <SimulationToggle />
      </div>
      <div className="mb-16">
        <p>Outside Temperature: <strong>...</strong></p>
      </div>
      <div className="mb-16">
        <p>Location: <strong>...</strong></p>
      </div>
      <div className="mb-16">
        <DateTimeDisplay />
      </div>
    </div>
  );
};

export default Profile;