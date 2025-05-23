import React, { useContext } from 'react';
import { RoomContext } from './RoomProvider';

const SimulationToggle = () => {
  const {isSimulationOn, setSimulationOn} = useContext(RoomContext);

  const toggleSimulation = () => {
    setSimulationOn(!isSimulationOn);
  };

  return (
    <div className="flex items-center justify-center">
        <span className="pr-3">Toggle Simulation:</span>
      <label className="inline-flex relative items-center cursor-pointer">
        <input type="checkbox" value={isSimulationOn} onChange={toggleSimulation} className="sr-only peer" />
        <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-500"></div>
      </label>
    </div>
  );
};

export default SimulationToggle;