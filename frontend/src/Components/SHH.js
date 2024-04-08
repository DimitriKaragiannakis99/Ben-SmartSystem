import React, { useState, useEffect, useRef, useContext } from "react";
import axios from "axios";
import { RoomContext } from "./Dashboard/RoomProvider";
import { OutputConsoleContext } from "./OutputConsoleProvider";
import { CurrentUserContext } from "./CurrentUserProvider";

function RoomInfo() {
  const { toggleHVAC, isSHHOn, rooms } = useContext(RoomContext);
  const [roomTemperatures, setRoomTemperatures] = useState([]);
  const [oldRoomTemps, setOldRoomTemperatures] = useState([]);
  const [alertTriggered, setAlertTriggered] = useState(true);

  let intervalRef = useRef();
  // added OutputConsoleContext
  const { consoleMessages, updateConsoleMessages } =
    useContext(OutputConsoleContext);

const {currSimUser, updateCurrSimUser} = useContext(CurrentUserContext);

  // Use this without dependency array to call all functions once when the page renders
  useEffect(() => {
    if (alertTriggered) {
      checkTemp();
    }
  });

  useEffect(() => {
    // const intervalHandler = () => {
    //   checkTemp();
    //   fetchRoomTemperatures();
    // };
    //checkTemp();
    fetchRoomTemperatures();
    //intervalRef.current = setInterval(intervalHandler, 1000);
    //return () => clearInterval(intervalRef.current); //cleanup on unmount
  }, [roomTemperatures]);

  //Method to check room temp continously
  const checkTemp = () => {
    axios
      .get("http://localhost:8080/api/temp/checkTemp")
      .then((response) => {
        if (response.data === "Temperature is normal") {
          //temp is normal do nothing
        } else {
          setAlertTriggered(false); // stop the check and send alert
          const currentTime = new Date().toLocaleTimeString();
          const message = `[${currentTime}] ${response.data} `;
          // updating OutputConsole context
          updateConsoleMessages(message);
          toggleAwayMode();
        }
      })
      .catch((error) => {
        console.error("Error checking room temp", error);
      });
  };

  const toggleAwayMode = () => {
    axios
      .get("http://localhost:8080/api/house/toggleAwayMode")
      .catch((error) => {
        console.error("Error toggling away mode", error);
      });
  };

  const setOldTemps = () => {
    axios
      .get("http://localhost:8080/api/getAllRooms")
      .then((response) => {
        setOldRoomTemperatures(response.data);
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  };

  const checkTempLimit = () => {
    //Now information in roomTemperature array is current so we can compare with old temps;
    console.log("This shit working");
    if (oldRoomTemps == null) {
      setOldTemps();
    }
    for (i = 0; i < oldRoomTemps.length; i++) {
      if (oldRoomTemps[i].temperature < roomTemperatures[i].temperature - 15) {
        console.log("ALERT");
        setAlertTriggered(false);
      }
      //We need to update the old temp with the new temps
    }
    oldRoomTemps = roomTemperatures;
  };

  const fetchRoomTemperatures = () => {
    axios
      .get("http://localhost:8080/api/getAllRooms")
      .then((response) => {
        setRoomTemperatures(response.data);
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  };

  //Function must send a get request to backend
  const turn_HVAC_on = (roomid) => {
    setAlertTriggered(true); // reset the check temp method
    axios
      .get("http://localhost:8080/api/temp/HVAC-on", {
        params: { roomID: roomid },
      })
      .then((response) => {
        console.log("hvac on");
        const roomName = rooms.find((room) => room.id === roomid)?.name;
        const currentDate = new Date().toLocaleDateString();
        const currentTime = new Date().toLocaleTimeString();
        const message = `[${currentDate}][${currentTime}] the HVAC has been turned on in ${roomName} by ${currSimUser} `;
        // updating OutputConsole context
        updateConsoleMessages(message);
        clearInterval(intervalRef.current);
        toggleHVAC(roomid);
      })
      .catch((error) => {
        console.error("Error turning on HVAC", error);
      });
  };

  const turn_HVAC_off = (roomid) => {
    axios
      .get("http://localhost:8080/api/temp/HVAC-off", {
        params: { roomID: roomid },
      })
      .then((response) => {
        console.log("hvac off");
        const roomName = rooms.find((room) => room.id === roomid)?.name;
        const currentDate = new Date().toLocaleDateString();
        const currentTime = new Date().toLocaleTimeString();
        const message = `[${currentDate}][${currentTime}] the HVAC has been turned off in ${roomName} by ${currSimUser} `;
         // updating OutputConsole context
         updateConsoleMessages(message);
        clearInterval(intervalRef.current);
        toggleHVAC(roomid);
      })
      .catch((error) => {
        console.error("Error turning off HVAC", error);
      });
  };

  if (!isSHHOn) {
    return <div className="bg-blue-100 min-h-screen">SHH is off.</div>;
  }

  return (
    <div className="inline-grid grid-cols-1 gap-4">
      {roomTemperatures.map((room) => (
        <div
          key={room.id}
          className="bg-gray-100 p-4 rounded-md border border-gray-300"
        >
          <div className="text-lg font-bold">{room.name}</div>
          <div className="text-xl">
            {parseFloat(room.temperature).toFixed(2)} °C
          </div>
          <div className="text-xl">
            Desired Room Temp:{" "}
            {room.isTemperatureOverridden
              ? `${room.desiredTemperature} (overridden)`
              : room.desiredTemperature}
          </div>
          <div className="text-xl">
            HVAC: {room.isHeaterOn || room.isAcOn ? "ON" : "OFF"}
          </div>
          <div>
            <button
              className="px-4 py-2 mt-2 border border-gray-300 bg-indigo-500 text-white rounded hover:bg-blue-700 transition-colors mr-2"
              onClick={() => turn_HVAC_on(room.id)}
              disabled={room.isAcOn || room.isHeaterOn}
            >
              Turn HVAC On
            </button>
            <button
              className="px-4 py-2 mt-2 border border-gray-300 bg-blue-500 text-white rounded hover:bg-blue-700 transition-colors mr-2"
              onClick={() => turn_HVAC_off(room.id)}
              disabled={!room.isAcOn && !room.isHeaterOn}
            >
              Turn HVAC Off
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}

export default RoomInfo;
