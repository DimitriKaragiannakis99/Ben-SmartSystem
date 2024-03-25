import React, { useState, useEffect, useRef } from "react";
import axios from "axios";

function RoomInfo() {
  const [roomTemperatures, setRoomTemperatures] = useState([]);
  let intervalRef = useRef();

  useEffect(() => {
    const intervalHandler = () => {
      checkTemp();
      fetchRoomTemperatures();
    };

    fetchRoomTemperatures();
    intervalRef.current = setInterval(intervalHandler, 1000);
    return () => clearInterval(intervalRef.current); //cleanup on unmount
  }, [roomTemperatures]);

  //Method to check room temp continously
  const checkTemp = () => {
    axios
      .get("http://localhost:8080/api/temp/checkTemp")
      .then((response) => {
        if (
          response.data.includes("Temperature is below zero, pipes may burst!")
        ) {
          // this should be logged on console if alert is triggered
          clearInterval(intervalRef.current);
        }
      })
      .catch((error) => {
        console.error("Error checking room temp", error);
      });
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
    axios
      .get("http://localhost:8080/api/temp/HVAC-on", {
        params: { roomID: roomid },
      })
      .then((response) => {
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
      })
      .catch((error) => {
        console.error("Error turning off HVAC", error);
      });
  };

  return (
    <div className="inline-grid grid-cols-1 gap-4">
      {roomTemperatures.map((room) => (
        <div
          key={room.id}
          className="bg-gray-100 p-4 rounded-md border border-gray-300"
        >
          <div className="text-lg font-bold">{room.name}</div>
          <div className="text-xl">{room.temperature} °C</div>
          <div className="text-xl">
          Desired Room Temp: {room.isTemperatureOverridden ? `${room.desiredTemperature} (overridden)` : room.desiredTemperature}
          </div>
          <div className="text-xl">
            Heater: {room.isHeaterOn ? "On" : "Off"}
          </div>
          <div className="text-xl">AC: {room.isAcOn ? "On" : "Off"}</div>
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
