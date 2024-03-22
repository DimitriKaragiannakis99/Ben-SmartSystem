import React, { useState, useEffect } from "react";
import axios from "axios";

const RoomZones = () => {
  const [rooms, setRooms] = useState([]);
  const [selectedRoomIds, setSelectedRoomIds] = useState([]);
  const [zones, setZones] = useState([]);

  useEffect(() => {
    const fetchRoomsAndZones = async () => {
      try {
        const roomResponse = await axios.get("http://localhost:8080/api/rooms");
        setRooms(roomResponse.data);

        const zoneResponse = await axios.get("http://localhost:8080/api/zones");
        setZones(zoneResponse.data);
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };

    fetchRoomsAndZones();
  }, []);

  

  const handleCheckboxChange = (roomId) => {
    setSelectedRoomIds(prev => {
      if (prev.includes(roomId)) {
        return prev.filter(id => id !== roomId);
      } else {
        return [...prev, roomId];
      }
    });
  };

  const handleAddToZone = () => {
    if (selectedRoomIds.length === 0) {
      alert("Please select at least one room.");
      return;
    }
    const zoneName = prompt("Enter a zone name:");
    if (zoneName) {
      const selectedRooms = rooms.filter(room => selectedRoomIds.includes(room.id));
      const zone = {
        name: zoneName,
        rooms: selectedRooms.map(room => room.name), // Convert IDs to names
      };

      axios.post("http://localhost:8080/api/zones", {
        name: zoneName,
        rooms: selectedRoomIds // Send IDs to backend
      })
        .then(response => {
          const newZone = {
            name: zoneName,
            rooms: selectedRooms.map(room => room.name), // Convert IDs to names
            id: response.data
          };
          setZones(prevZones => [...prevZones, newZone]);
          setSelectedRoomIds([]);
        })
        .catch(error => {
          console.error("Error creating zone:", error);
          alert("Error creating zone: " + error.response.data);
        });
    }
  };

  return (
    <div>
      <ul>
        {rooms.map(room => (
          <li key={room.id}>
            <label>
              <input
                type="checkbox"
                checked={selectedRoomIds.includes(room.id)}
                onChange={() => handleCheckboxChange(room.id)}
              />
              {room.name}
            </label>
          </li>
        ))}
      </ul>
      <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" onClick={handleAddToZone}>
        Add to Zone
      </button>
      {zones.map(zone => (
        <div key={zone.id}>
          <strong>{zone.name}</strong>
          {zone.rooms && (
            <ul>
              {zone.rooms.map(roomName => (
                <li key={roomName}>{roomName}</li>
              ))}
            </ul>
          )}
        </div>
      ))}
    </div>
  );
};

export default RoomZones;