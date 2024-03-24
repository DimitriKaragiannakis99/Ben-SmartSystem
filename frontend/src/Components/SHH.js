import React, { useState, useEffect, useRef } from "react";
import axios from "axios";

function RoomInfo() {
  const [roomTemperatures, setRoomTemperatures] = useState([]);
  let intervalRef = useRef();

  useEffect(() => {
    fetchRoomTemperatures();
    intervalRef.current = setInterval(checkTemp, 1000);
    return () => clearInterval(intervalRef.current); //cleanup on unmount
  }, []);

  //Method to check room temp continously
  const checkTemp = () => {
    axios
      .get("http://localhost:8080/api/temp/checkTemp")
      .then((response) => {
        if (
          response.data.includes("Temperature is below zero, pipes may burst!")
        ) {
          // this should be logged on console if alert is triggered
          console.log(response);
          clearInterval(intervalRef.current);
        }
      })
      .catch((error) => {
        console.error("Error checking room temp", error);
      });
  };

  const fetchRoomTemperatures = () => {
    axios
      .get("http://localhost:8080/api/rooms")
      .then((response) => {
        console.log(response.data);
        setRoomTemperatures(response.data);
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  };

  const turn_HVAC_on = () => {};

  const turn_HVAC_off = () => {};

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
            Desired Room Temp: {room.desiredTemperature} °C
          </div>
          <div className="text-xl">
            Heater: {room.isHeaterOn ? "On" : "Off"}
          </div>
          <div className="text-xl">AC: {room.isAcOn ? "On" : "Off"}</div>
        </div>
      ))}
    </div>
  );
}

export default RoomInfo;
