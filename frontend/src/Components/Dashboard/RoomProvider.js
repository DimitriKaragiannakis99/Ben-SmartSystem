import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const RoomContext = createContext();

const RoomProvider = ({ children }) => {
  const [rooms, setRooms] = useState([]);

  const [isSimulationOn, setSimulationOn] = useState(false);

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
            room.id === roomId ? { ...room, windowBlocked: !room.windowBlocked } : room,
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
            room.id === roomId ? { ...room, windowOpen: !room.windowOpen } : room,
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
            room.id === roomId ? { ...room, doorOpen: !room.doorOpen } : room,
          ),
        );
      })
      .catch((error) => {
        console.error('Error toggling door', error);
      });
  }

  return (
    <RoomContext.Provider value={{ rooms, toggleLight, toggleWindowBlocked, toggleWindow, toggleDoor, isSimulationOn, setSimulationOn }}>
      {children}
    </RoomContext.Provider>
  );
};

export default RoomProvider;
