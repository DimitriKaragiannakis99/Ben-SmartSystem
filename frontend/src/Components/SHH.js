import React, { useState, useEffect } from "react";
import axios from "axios";

function RoomInfo() {
  const [roomTemperatures, setRoomTemperatures] = useState([]);

  useEffect(() => {
    fetchRoomTemperatures();
  }, []);

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

  return (
    <div className="inline-grid grid-cols-1 gap-4">
      {roomTemperatures.map((room) => (
        <div
          key={room.id}
          className="bg-gray-100 p-4 rounded-md border border-gray-300"
        >
          <div className="text-lg font-bold">{room.name}</div>
          <div className="text-xl">{room.temperature} °C</div>
        </div>
      ))}
    </div>
  );
}

export default RoomInfo;
