import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const RoomContext = createContext();

const RoomProvider = ({ children }) => {
  const [rooms, setRooms] = useState([]);

  const [isSimulationOn, setSimulationOn] = useState(false);
  const [isSHHOn, setIsSHHOn] = useState(false);

  useEffect(() => {
    axios.get('http://localhost:8080/api/rooms')
      .then((response) => {
        setRooms(response.data.map(room => ({
          ...room,
          isLightOn: room.isLightOn,
          windowBlocked: room.isWindowBlocked
        })));
      })
      .catch((error) => {
        console.error('Error fetching room information', error);
      });
  }, []);

  const toggleLight = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleLight?roomId=${roomId}`)
      .then(() => {
        setRooms(prevRooms =>
          prevRooms.map(room =>
            room.id === roomId ? { ...room, isLightOn: !room.isLightOn } : room,
          ),
        );
      })
      .catch((error) => {
        console.error('Error toggling light', error);
      });
  };

  const toggleWindowBlocked = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleWindow?roomId=${roomId}`)
      .then(() => {
        setRooms(prevRooms =>
          prevRooms.map(room =>
            room.id === roomId ? { ...room, isWindowBlocked: !room.isWindowBlocked } : room,
          ),
        );
      })
      .catch((error) => {
        console.error('Error toggling window', error);
      });
  };

  const toggleWindow = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleWindow?roomId=${roomId}`)
      .then(() => {
        setRooms(prevRooms =>
          prevRooms.map(room =>
            room.id === roomId ? { ...room, isWindowOpen: !room.isWindowOpen } : room,
          ),
        );
      })
      .catch((error) => {
        console.error('Error toggling window', error);
      });
  }

  const toggleDoor = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleDoor?roomId=${roomId}`)
      .then(() => {
        setRooms(prevRooms =>
          prevRooms.map(room =>
            room.id === roomId ? { ...room, isDoorOpen: !room.isDoorOpen } : room,
          ),
        );
      })
      .catch((error) => {
        console.error('Error toggling door', error);
      });
  }

  const toggleHVAC = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleHVAC?roomId=${roomId}`)
      .then(() => {
        setRooms(prevRooms =>
          prevRooms.map(room =>
            room.id === roomId ? { ...room, isHVACOn: !room.isHVACOn } : room,
          ),
        );
      })
      .catch((error) => {
        console.error('Error toggling HVAC', error);
      });
  }

  const updateRoomTemperature = (roomId, newTemperature, isOverridden) => {
    setRooms((prevRooms) =>
      prevRooms.map((room) => {
        if (room.id === roomId) {
          return { ...room, desiredTemperature: newTemperature, isTemperatureOverridden: isOverridden };
        }
        return room;
      })
    );
  };  

  return (
    <RoomContext.Provider value={{ rooms, toggleLight, toggleWindowBlocked, toggleWindow, toggleDoor, isSimulationOn, setSimulationOn, isSHHOn, setIsSHHOn, updateRoomTemperature, toggleHVAC }}>
      {children}
    </RoomContext.Provider>
  );
};

export default RoomProvider;