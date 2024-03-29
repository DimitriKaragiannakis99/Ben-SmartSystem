import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { RoomContext } from "./RoomProvider";

const DashboardSHP = () => {
    const [rooms, setRooms] = useState([]);
    const { toggleMotionDetector } = useContext(RoomContext);

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

    return (
        <div>
            {rooms.map((room) => (
          <ul key={room.id}>
            {room.name}
            <button 
                onClick={() => toggleMotionDetector(room.id)}
                className="ml-2 bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded"
            >
                Toggle Motion Detector
            </button>
          </ul>
        ))}
        </div>
    )
};

export default DashboardSHP;