import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { RoomContext } from "./Dashboard/RoomProvider";
import { OutputConsoleContext } from "./OutputConsoleProvider";

function SHC() {
  const {
    toggleLight,
    toggleWindow,
    toggleDoor,
    currentSimUser,
    fetchCurrentSimUser,
  } = useContext(RoomContext); // Destructure toggleLight, toggleWindow, and toggleDoor functions
  const [rooms, setRooms] = useState([]);
  const [simulatorUser, setSimulatorUser] = useState("Parents");
  const [selectedRooms, setSelectedRooms] = useState({});
  const [selectedComponent, setSelectedComponent] = useState("");
  const [isAwayMode, setIsAwayMode] = useState(false);

  // added OutputConsoleContext
  const { consoleMessages, updateConsoleMessages } =
    useContext(OutputConsoleContext);

  // state for auto lights
  const [autoLight, setAutoLight] = useState(false);

  // state for auto locks
  const [autoLock, setAutoLock] = useState(false);

  useEffect(() => {
    // Define the requests
    const fetchRooms = axios.get("http://localhost:8080/api/rooms");
    const fetchAwayMode = axios.get("http://localhost:8080/api/house/awayMode");

    Promise.all([fetchRooms, fetchAwayMode])
      .then((responses) => {
        // responses[0] corresponds to the rooms, responses[1] to the away mode
        const roomsResponse = responses[0];
        const awayModeResponse = responses[1];

        setRooms(roomsResponse.data);
        setIsAwayMode(awayModeResponse.data.isAwayModeOn);
      })
      .catch((error) => {
        // This will catch any error that was thrown during the fetching of rooms or away mode
        console.error("Error fetching data", error);
      });
  }, []);

  const handleRoomCheckChange = (roomName) => {
    setSelectedRooms((prevSelectedRooms) => ({
      ...prevSelectedRooms,
      [roomName]: !prevSelectedRooms[roomName],
    }));

    console.log(selectedRooms);
  };

  const handleComponentSelection = (componentName) => {
    setSelectedComponent(componentName);
  };

  const selectAllRooms = () => {
    const allSelected = rooms.reduce((acc, room) => {
      acc[room.name] = true;
      return acc;
    }, {});
    setSelectedRooms(allSelected);
  };

  const deselectAllRooms = () => {
    const noneSelected = rooms.reduce((acc, room) => {
      acc[room.name] = false;
      return acc;
    }, {});
    setSelectedRooms(noneSelected);
  };

  const handleOpenCloseAction = (action) => {
    const selectedRoomNames = Object.keys(selectedRooms).filter(
      (roomName) => selectedRooms[roomName]
    );

    if (!selectedComponent || selectedRoomNames.length === 0) {
      console.log("No component or room selected.");
      return;
    }

    console.log(selectedComponent);

    console.log(consoleMessages);

    const currentTime = new Date().toLocaleTimeString();
    const actionText = action === "open" ? "opened" : "closed";
    const message = `[${currentTime}] [${selectedComponent}] in ${selectedRoomNames.join(
      ", "
    )} was ${actionText} by ${simulatorUser} request.`;

    // updating OutputConsole context
    updateConsoleMessages(message);

    if (selectedComponent === "light") {
      selectedRoomNames.forEach((roomName) => {
        const room = rooms.find((r) => r.name === roomName);
        if (room) {
          toggleLight(room.id);
        }
      });
    } else if (selectedComponent === " window") {
      //Check if in Away mode
      selectedRoomNames.forEach((roomName) => {
        const room = rooms.find((r) => r.name === roomName);
        if (room) {
          toggleWindow(room.id);
        }
      });
    } else if (selectedComponent === " door") {
      //Check if in Away mode
      selectedRoomNames.forEach((roomName) => {
        const room = rooms.find((r) => r.name === roomName);
        if (room) {
          toggleDoor(room.id);
        }
      });
    }
  };

  const handleAutoLight = (action) => {
    const currentTime = new Date().toLocaleTimeString();

    if (action === "on") {
      setAutoLight(true);
    } else {
      setAutoLight(false);
    }

    const actionText = action === "on" ? "activated" : "deactivated";
    const message = `[${currentTime}] [Auto Lights] was ${actionText} by ${simulatorUser} request.`;

    // updating OutputConsole context
    updateConsoleMessages(message);
  };

  const handleAutoLock = (action) => {
    const currentTime = new Date().toLocaleTimeString();

    if (action === "on") {
      setAutoLock(true);
    } else {
      setAutoLock(false);
    }

    const actionText = action === "on" ? "activated" : "deactivated";
    const message = `[${currentTime}] [Auto Locks] was ${actionText} by ${simulatorUser} request.`;

    // updating OutputConsole context
    updateConsoleMessages(message);
  };

  return (
    <>
      <div className="container bg-blue-500 mx-auto my-8 p-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
          <div className="md:col-span-3 md:order-1">
            <div className="mb-4 p-4 border border-gray-200 rounded">
              <h2 className="font-bold mb-3">Room Components</h2>
              <div>
                {rooms.length > 0 && (
                  <ul>
                    {rooms[0].roomComponents.map((component, index) => (
                      <li
                        key={index}
                        onClick={() => handleComponentSelection(component)}
                        style={{
                          cursor: "pointer",
                          backgroundColor:
                            selectedComponent === component
                              ? "#efefef"
                              : "transparent",
                          padding: "5px",
                          margin: "5px",
                          borderRadius: "5px",
                        }}
                      >
                        {component}
                      </li>
                    ))}
                  </ul>
                )}
              </div>
              <div>
                <button
                  className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
                  onClick={() => handleOpenCloseAction("open")}
                >
                  Open
                </button>
                <button
                  className="px-4 py-2 border border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors"
                  onClick={() => handleOpenCloseAction("close")}
                >
                  Close
                </button>
              </div>
            </div>

            {/* Box for rooms */}
            <div className="p-4 border border-gray-200 rounded">
              <h2 className="font-bold mb-3">Rooms</h2>
              <ul>
                {rooms.map((room) => (
                  <li key={room.id}>
                    <label className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        checked={selectedRooms[room.name] || false}
                        onChange={() => handleRoomCheckChange(room.name)}
                      />
                      <span>{room.name}</span>
                    </label>
                  </li>
                ))}
              </ul>
              <button
                onClick={selectAllRooms}
                className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-blue-700 transition-colors"
              >
                All
              </button>
              <button
                onClick={deselectAllRooms}
                className="px-4 py-2 border  border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors ml-2"
              >
                None
              </button>
            </div>
            {/* Box for the auto lights */}

            <div className=" mt-4 p-4 border border-gray-200 rounded">
              <h2 className="font-bold mb-3">Auto Lights</h2>
              <div>
                <button
                  className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
                  onClick={() => handleAutoLight("on")}
                >
                  On
                </button>
                <button
                  className="px-4 py-2 border border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors"
                  onClick={() => handleAutoLight("off")}
                >
                  Off
                </button>
              </div>
            </div>

            {/* Box for the auto locks */}

            <div className=" mt-4 p-4 border border-gray-200 rounded">
              <h2 className="font-bold mb-3">Auto Locks</h2>
              <div>
                <button
                  className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
                  onClick={() => handleAutoLock("on")}
                >
                  On
                </button>
                <button
                  className="px-4 py-2 border border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors"
                  onClick={() => handleAutoLock("off")}
                >
                  Off
                </button>
              </div>
            </div>
          </div>
        </div>

        <div className="p-4 border border-gray-200 rounded h-48 overflow-auto">
          <h2 className="font-bold mb-3">Output Console</h2>
          {consoleMessages.map((message, index) => (
            <p key={index}>{message}</p>
          ))}
        </div>
      </div>
    </>
  );
}

export default SHC;
