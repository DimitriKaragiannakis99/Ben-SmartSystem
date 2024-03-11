import React, { useContext, useState } from 'react';
import { RoomContext } from './RoomProvider';

function UserFigure({ name }) {
  return (
    <div className="text-center m-2.5">
      <div>{name}</div>
      <img src="/images/stickman.png" alt="User" className="w-12 h-auto" />
    </div>
  );
}

function LightbulbFigure() {
  return (
    <div className="text-center m-2.5">
      <img src="/images/lightbulb.png" alt="Lightbulb" className="w-12 h-auto" />
    </div>
  );
}

function WindowFigure() {
  return (
    <div className="text-center m-2.5">
      <img src="/images/window.png" alt="Window" className="w-12 h-auto" />
    </div>
  );
}

function DoorFigure() {
  return (
    <div className="text-center m-2.5">
      <img src="/images/door.png" alt="Door" className="w-12 h-auto" />
    </div>
  );
}


const RoomView = ({ room }) => {
  const [visibleElements, setVisibleElements] = useState({
    lightbulb: room.isLightOn,
    window: room.isWindowOpen,
    door: room.isDoorOpen
  });

  const toggleElement = (element) => {
    setVisibleElements(prevState => ({ ...prevState, [element]: !prevState[element] }));
  };

  return (
    <div className="flex">
      <div className="flex flex-col items-center w-96 h-96 border-2 border-dashed border-gray-300 rounded-lg p-2 bg-gray-50">
        <h2 className="text-center text-lg font-bold mb-2">{room.name}</h2>
        <div className="flex-1 w-full">
          {room.users.map((userId) => {
            const userName = `User ${userId.split("-")[1]}`;
            return (
              <div key={userId} className="flex items-center justify-center m-1 p-2 w-36 rounded bg-white">
                <UserFigure name={userName} />
              </div>
            );
          })}
          {visibleElements.lightbulb && <LightbulbFigure />}
          {visibleElements.window && <WindowFigure />}
          {visibleElements.door && <DoorFigure />}
        </div>
      </div>
      <div className={`w-32 h-32 border-2 border-dashed border-gray-300 rounded-lg p-2 pr-2 flex flex-col items-center justify-center ${room.windowBlocked ? 'bg-blue-200' : 'bg-gray-50'}`} style={{ minHeight: '100px', minWidth: '100px' }}>
        <h3 className="font-bold">Window</h3>
        {room.windowBlocked && (
          <div className="text-red-500 text-center">Window Blocked</div>
        )}
      </div>
      <div className="flex flex-col ml-4">
        <button onClick={() => toggleElement('lightbulb')}>Toggle Lightbulb</button>
        <button onClick={() => toggleElement('window')}>Toggle Window</button>
        <button onClick={() => toggleElement('door')}>Toggle Door</button>
      </div>
    </div>
  );
};


const HouseView = () => {
  const { rooms, isSimulationOn } = useContext(RoomContext);

  if (!isSimulationOn) {
    return <div className="bg-blue-100 min-h-screen">Simulation is off.</div>;
  }

  return (
    <div className="bg-blue-100 min-h-screen">
      <div className="flex justify-between items-center p-4">
      <div className="w-full text-center">
          <h1 className="text-2xl font-bold inline-block">House View</h1>
        </div>
        <div>
          <label htmlFor="awayModeToggle" className="mr-2">Away Mode:</label>
          <input type="checkbox" id="awayModeToggle" />
        </div>
      </div>
      <div className="flex flex-wrap justify-center gap-4 p-4">
        {rooms.map((room) => (
          <RoomView key={room.id} room={room} />
        ))}
      </div>
    </div>
  );
};

export default HouseView;