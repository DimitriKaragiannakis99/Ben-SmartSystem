import React, { createContext, useState, useEffect } from "react";
import axios from "axios";

export const RoomContext = createContext();

const RoomProvider = ({ children }) => {
  const [rooms, setRooms] = useState([]);

  const [isSimulationOn, setSimulationOn] = useState(false);

  const [visibleElements, setVisibleElements] = useState({
    lightbulb: true,
    window: true,
    door: true,
  });

  const setElementVisibility = (roomId, element, isVisible) => {
    setVisibleElements((prevState) => ({
      ...prevState,
      [roomId]: {
        ...prevState[roomId],
        [element]: isVisible,
      },
    }));
  };

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/rooms")
      .then((response) => {
        const initialVisibleElements = {};
        response.data.forEach((room) => {
          initialVisibleElements[room.id] = {
            windowBlocked: room.isWindowBlocked,
            lightbulb: room.isLightOn,
            window: room.isWindowOpen,
            door: room.isDoorOpen,
          };
        });
        setVisibleElements(initialVisibleElements);
        setRooms(response.data);
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  }, []);

  const toggleLight = (roomId) => {
    console.log("Toggling light for room", roomId);
    fetch(`http://localhost:8080/api/toggleLight?roomId=${roomId}`, {
      method: "POST", // Specify the method
      headers: {
        // Specify any necessary headers
        // 'Content-Type': 'application/json', // Uncomment if you need to send a body with JSON content
      },
      // body: JSON.stringify(data), // Uncomment and replace `data` if you need to send a body
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.text(); // or response.json() if the response is JSON
      })
      .then((data) => {
        console.log(data);
        fetchRooms(); // Fetch updated rooms from the backend
      })
      .catch((error) => {
        console.error("Error toggling light", error);
      });
  };

  const toggleWindow = (roomId) => {
    axios
      .post(`http://localhost:8080/api/toggleWindow`, null, {
        params: { roomId },
      })
      .then(() => {
        fetchRooms(); // Fetch updated rooms from the backend
      })
      .catch((error) => {
        console.error("Error toggling window", error);
      });
  };

  const toggleDoor = (roomId) => {
    axios
      .post(`http://localhost:8080/api/toggleDoor`, null, {
        params: { roomId },
      })
      .then(() => {
        fetchRooms(); // Fetch updated rooms from the backend
      })
      .catch((error) => {
        console.error("Error toggling door", error);
      });
  };

  // Helper function to fetch updated rooms from the backend
  const fetchRooms = () => {
    axios
      .get("http://localhost:8080/api/rooms")
      .then((response) => {
        setRooms(response.data);
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  };

  return (
    <RoomContext.Provider
      value={{
        rooms,
        toggleLight,
        toggleWindow,
        toggleDoor,
        isSimulationOn,
        setSimulationOn,
        visibleElements,
        setElementVisibility,
      }}
    >
      {children}
    </RoomContext.Provider>
  );
};

export default RoomProvider;
