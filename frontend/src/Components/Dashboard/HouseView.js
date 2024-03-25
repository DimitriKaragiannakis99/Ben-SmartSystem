import React, { useContext } from "react";
import { RoomContext } from "./RoomProvider";

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
      <img
        src="/images/lightbulb.png"
        alt="Lightbulb"
        className="w-12 h-auto"
      />
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

function HVACFigure() {
  return (
    <div className="text-center m-2.5">
      <img src="/images/heater.jpg" alt="Heater" className="w-12 h-auto" />
    </div>
  );
}

const HouseView = () => {
  const { rooms, isSimulationOn } = useContext(RoomContext);

  if (!isSimulationOn) {
    return <div className="bg-blue-100 min-h-screen">Simulation is off.</div>;
  }

  return (
    <div className="bg-blue-100 min-h-screen">
      <div className="flex flex-wrap justify-center gap-4 p-4">
        {rooms.map((room) => (
          <div key={room.id} className="flex">
            <div className="flex flex-col items-center w-96 h-96 border-2 border-dashed border-gray-300 rounded-lg p-2 bg-gray-50">
              <div className="flex items-center justify-center">
                <h2 className="text-center text-lg font-bold mb-2">
                  {room.name}
                </h2>
                <h3 className="text-center text-sm mb-2">
                  {room.temperature} °C
                </h3>
              </div>
              <div className="flex">
                <div className="flex-1">
                  {room.users.map((userId) => {
                    const userName = userId;
                    return (
                      <div
                        key={userId}
                        className="flex items-center justify-center m-1 p-2 w-36 rounded bg-white"
                      >
                        <UserFigure name={userName} />
                      </div>
                    );
                  })}
                </div>
                <div className="flex flex-col items-center justify-start ml-4">
                  {room.isLightOn && <LightbulbFigure />}
                  {room.isWindowOpen && <WindowFigure />}
                  {room.isDoorOpen && <DoorFigure />}
                  {room.isHVACOn && <HVACFigure />}
                </div>
              </div>
            </div>
            <div
              className={`w-32 h-32 border-2 border-dashed border-gray-300 rounded-lg p-2 pr-2 flex flex-col items-center justify-center ${
                room.windowBlocked ? "bg-blue-200" : "bg-gray-50"
              }`}
              style={{ minHeight: "100px", minWidth: "100px" }}
            >
              <h3 className="font-bold">Window</h3>
              {room.windowBlocked && (
                <div className="text-red-500 text-center">Window Blocked</div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default HouseView;
