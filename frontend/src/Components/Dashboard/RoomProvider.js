import React, { createContext, useState, useEffect ,useContext } from 'react';
import { OutputConsoleContext } from "../OutputConsoleProvider"
import { CurrentUserContext } from "../CurrentUserProvider";
import axios from 'axios';

export const RoomContext = createContext();

const RoomProvider = ({ children }) => {
  const [rooms, setRooms] = useState([]);

  const [isSimulationOn, setSimulationOn] = useState(false);
  const [isSHHOn, setIsSHHOn] = useState(false);
  const [currentSimUser, setCurrentSimUser] = useState({ id: '', username: '' });
  const { consoleMessages, updateConsoleMessages } =
    useContext(OutputConsoleContext);

  const {currSimUser, updateCurrSimUser} = useContext(CurrentUserContext);
  

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

  const setAllWindowsAndDoorsOff = () => {
    //Set all rooms' windows and doors to be closed
    setRooms((prevRooms) =>
      prevRooms.map((room) => ({
        ...room,
        isWindowOpen: false,
        isDoorOpen: false,
      }))
    );
       
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

/*
  const toggleHasMotionDetector = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleHasMotionDetector?roomId=${roomId}`)
        .then(() => {
            setRooms((prevRooms) =>
                prevRooms.map((room) =>
                    room.id === roomId ? { ...room, hasMotionDetector: !room.hasMotionDetector } : room
                )
            );
        })
        .catch((error) => {
            console.error("Error toggling motion detector", error);
        });
  };*/

  const toggleHasMotionDetector = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleHasMotionDetector?roomId=${roomId}`)
        .then(() => {
            setRooms((prevRooms) =>
                prevRooms.map((room) => {
                   
                  const currentDate = new Date().toLocaleDateString();
                  const currentTime = new Date().toLocaleTimeString();
                  if (room.id === roomId) {
                        if (!room.hasMotionDetector) {
                          
                            const message1 = `[${currentDate}][${currentTime}] the hasMotionDetector has been turned on in ${room.name} by ${currSimUser} request.`;
                            updateConsoleMessages(message1);
                        } else {
                          const message2 = `[${currentDate}][${currentTime}] the hasMotionDetector has been turned off in ${room.name} by ${currSimUser} request.`;
                          updateConsoleMessages(message2);
                        }
                        return { ...room, hasMotionDetector: !room.hasMotionDetector };
                    } else {
                        return room;
                    }
                })
            );
        })
        .catch((error) => {
            console.error("Error toggling motion detector", error);
        });
};

/*
  const toggleMotionDetector = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleMotionDetector?roomId=${roomId}`)
        .then(() => {
            setRooms((prevRooms) =>
                prevRooms.map((room) =>
                    room.id === roomId ? { ...room, isMotionDetectorOn: !room.isMotionDetectorOn } : room
                )
            );
        })
        .catch((error) => {
            console.error("Error toggling motion detector", error);
        });
  };*/

  const toggleMotionDetector = (roomId) => {
    axios.post(`http://localhost:8080/api/toggleMotionDetector?roomId=${roomId}`)
        .then(() => {
            setRooms((prevRooms) =>
                prevRooms.map((room) => {
                    const currentDate = new Date().toLocaleDateString();
                    const currentTime = new Date().toLocaleTimeString();
                    if (room.id === roomId) {
                        if (!room.isMotionDetectorOn) {
                            const message1 = `[${currentDate}][${currentTime}] the motion detector has been turned on in ${room.name} by ${currSimUser} request.`;
                            updateConsoleMessages(message1);
                        } else {
                            const message2 = `[${currentDate}][${currentTime}] the motion detector has been turned off in ${room.name} by ${currSimUser} request.`;
                            updateConsoleMessages(message2);
                        }
                        return { ...room, isMotionDetectorOn: !room.isMotionDetectorOn };
                    } else {
                        return room;
                    }
                })
            );
        })
        .catch((error) => {
            console.error("Error toggling motion detector", error);
        });
};


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
    <RoomContext.Provider value={{ rooms, toggleLight, toggleWindowBlocked, toggleWindow, toggleDoor, isSimulationOn, setSimulationOn, isSHHOn, setIsSHHOn, updateRoomTemperature, toggleHVAC, toggleHasMotionDetector, toggleMotionDetector, setAllWindowsAndDoorsOff }}>
      {children}
    </RoomContext.Provider>
  );
};

export default RoomProvider;