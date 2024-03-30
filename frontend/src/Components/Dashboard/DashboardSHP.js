import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { RoomContext } from "./RoomProvider";

const DashboardSHP = () => {
    const [rooms, setRooms] = useState([]);
    const [isAwayModeOn, setIsAwayModeOn] = useState(false);
    const { toggleHasMotionDetector, toggleMotionDetector } = useContext(RoomContext);

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

          axios.get('http://localhost:8080/api/house/awayMode')
          .then(response => {
              setIsAwayModeOn(response.data.isAwayModeOn);
              console.log('Away mode state:', response.data.isAwayModeOn);
          })
          .catch(error => {
              console.error('Error fetching away mode state', error);
          });
      }, []);

      const toggleAwayMode = () => {
        axios.post('http://localhost:8080/api/house/toggleAwayMode')
            .then(response => {
                if(response.data && typeof response.data.isAwayModeOn === 'boolean') {
                    setIsAwayModeOn(response.data.isAwayModeOn);
                    console.log('Away mode toggled. Current state:', response.data.isAwayModeOn);
                } else {
                    console.error('Unexpected response structure:', response.data);
                }
            })
            .catch((error) => {
                console.error('Error toggling away mode:', error);
            });
    };

    return (
        <div>
          <p>{isAwayModeOn ? 'Away Mode: ON' : 'Away Mode: OFF'}</p>
          <button 
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded"
            onClick={toggleAwayMode}>{isAwayModeOn ? 'Turn Off Away Mode' : 'Turn On Away Mode'}
          </button>
            {rooms.map((room) => (
          <ul key={room.id}>
            {room.name}
            <button 
                onClick={() => toggleHasMotionDetector(room.id)}
                className="ml-2 bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded"
            >
                Toggle Has Motion Detector
            </button>
            {room.hasMotionDetector && (
                <button 
                onClick={() => toggleMotionDetector(room.id)}
                className="ml-2 bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded">
                    Toggle Motion Detector
                </button>
            )}
          </ul>
        ))}
        </div>
    )
};

export default DashboardSHP;