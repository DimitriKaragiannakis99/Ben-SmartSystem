import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { RoomContext } from "./RoomProvider";
import { OutputConsoleContext } from "../OutputConsoleProvider"
import { CurrentUserContext } from "../CurrentUserProvider";

const DashboardSHP = () => {
    const [rooms, setRooms] = useState([]);
    const [isAwayModeOn, setIsAwayModeOn] = useState(false);
    const { toggleHasMotionDetector, toggleMotionDetector, setAllWindowsAndDoorsOff } = useContext(RoomContext);
    const [timerDelay, setTimerDelay] = useState(30);
    const {consoleMessages, updateConsoleMessages} = useContext(OutputConsoleContext);
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

          axios.get('http://localhost:8080/api/house/awayMode')
          .then(response => {
              setIsAwayModeOn(response.data.isAwayModeOn);
              console.log('Away mode state:', response.data.isAwayModeOn);
          })
          .catch(error => {
              console.error('Error fetching away mode state', error);
          });
      }, []);

      useEffect(() => {
        const intervalId = setInterval(() => {
            axios.get('http://localhost:8080/api/house/checkPoliceCalled')
                .then(response => {
                    if (response.data) {
                        alert('Police have been called!');
                        clearInterval(intervalId); // Stop polling once the alert is triggered
                        const currentDate = new Date().toLocaleDateString();
                        const currentTime = new Date().toLocaleTimeString();
                        const message = `[${currentDate}][${currentTime}] The Police have been called. `;
                        updateConsoleMessages(message);
                    }
                })
                .catch(error => console.error('Error checking if police have been called:', error));
        }, 1000); //poll every second
    
        return () => clearInterval(intervalId);
    }, []);

    useEffect(() => {
      axios.get('http://localhost:8080/api/house/getTimerDelay')
          .then(response => {
              setTimerDelay(response.data.delayInSeconds);
          })
          .catch(error => {
              console.error('Error fetching timer delay:', error);
          });
  }, []);

      const toggleAwayMode = () => {
        axios.post('http://localhost:8080/api/house/toggleAwayMode')
            .then(response => {
                if(response.data && typeof response.data.isAwayModeOn === 'boolean') {
                    setIsAwayModeOn(response.data.isAwayModeOn);
                    console.log('Away mode toggled. Current state:', response.data.isAwayModeOn);
                    //Update the simulation screen
                    setAllWindowsAndDoorsOff();
                    const currentDate = new Date().toLocaleDateString();
                    const currentTime = new Date().toLocaleTimeString();
                    let awayModeVal = "on";

                    if(response.data.isAwayModeOn){
                        awayModeVal = "on";
                    }else{
                        awayModeVal = "off";
                    }
                    const message = `[${currentDate}][${currentTime}] the away mode has been turned ${awayModeVal} by ${currSimUser} `;
                    updateConsoleMessages(message);
                } else {
                    console.error('Unexpected response structure:', response.data);
                }
            })
            .catch((error) => {
                console.error('Error toggling away mode:', error);
            });
    };

    const updateTimerDelay = () => {
        axios.post('http://localhost:8080/api/house/updateTimer', null, {
            params: { delayInSeconds: timerDelay }
        })
        .then(response => {
            console.log(response.data);
            const currentDate = new Date().toLocaleDateString();
            const currentTime = new Date().toLocaleTimeString();

            const message = `[${currentDate}][${currentTime}]The timer delay was updated by ${timerDelay} seconds by ${currSimUser} `;
            updateConsoleMessages(message);
        })
        .catch(error => {
            console.error('Error updating timer delay:', error);
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
        <input 
            type="number" 
            value={timerDelay} 
            onChange={(e) => setTimerDelay(e.target.value)} 
            className="w-10"
        />
        <button 
        onClick={updateTimerDelay}
        className="ml-2 bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded"
        >Update Timer Delay</button>
        </div>
    )
};

export default DashboardSHP;