import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { RoomContext } from "./RoomProvider";

const RoomZones = () => {
  const [rooms, setRooms] = useState([]);
  const [selectedRoomIds, setSelectedRoomIds] = useState([]);
  const [zones, setZones] = useState([]);
  const { updateRoomTemperature } = useContext(RoomContext);

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
    setSelectedRoomIds((prev) => {
      if (prev.includes(roomId)) {
        return prev.filter((id) => id !== roomId);
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
      const selectedRooms = rooms.filter((room) =>
        selectedRoomIds.includes(room.id)
      );
      const zone = {
        name: zoneName,
        rooms: selectedRooms.map((room) => room.name),
      };

      axios
        .post("http://localhost:8080/api/zones", {
          name: zoneName,
          rooms: selectedRoomIds,
        })
        .then((response) => {
          const newZone = {
            name: zoneName,
            rooms: selectedRooms.map((room) => room.name),
            id: response.data,
          };
          setZones((prevZones) => [...prevZones, newZone]);
          setSelectedRoomIds([]);
        })
        .catch((error) => {
          console.error("Error creating zone:", error);
          alert("Error creating zone: " + error.response.data);
        });
    }
  };

  const handleModifyTemperature = (zoneId) => {
    const cleanedZoneId = zoneId.replace(/\D/g, "");
    const newTemperature = prompt("Enter the new temperature:");
    if (
      newTemperature !== null &&
      newTemperature.trim() !== "" &&
      newTemperature >= -40 &&
      newTemperature <= 40
    ) {
      axios
        .put(`http://localhost:8080/api/zones/${cleanedZoneId}/temperature`, {
          temperature: newTemperature,
        })
        .then((response) => {
          setZones((prevZones) =>
            prevZones.map((zone) => {
              if (zone.id === zoneId) {
                return { ...zone, temperature: newTemperature };
              } else {
                return zone;
              }
            })
          );
          alert("Temperature updated successfully.");
        })
        .catch((error) => {
          console.error("Error updating temperature:", error);
          alert("Error updating temperature.");
        });
    }
  };

  const handleModifyRoomOverriddenTemperature = (roomId) => {
    const newTemperature = prompt("Set the new desired temperature:");
    if (
      newTemperature !== null &&
      newTemperature.trim() !== "" &&
      newTemperature >= -40 &&
      newTemperature <= 40
    ) {
      axios
        .put(`http://localhost:8080/api/rooms/${roomId}/temperature`, {
          temperature: newTemperature,
        })
        .then(() => {
          updateRoomTemperature(roomId, newTemperature);
          alert("Desired Temperature updated successfully.");
        })
        .catch((error) => {
          console.error("Error updating temperature:", error);
          alert("Error updating temperature.");
        });
    }
  };

  return (
    <div>
      <ul>
        {rooms.map((room) => (
          <li key={room.id}>
            <label>
              <input
                type="checkbox"
                checked={selectedRoomIds.includes(room.id)}
                onChange={() => handleCheckboxChange(room.id)}
              />
              {room.name}
            </label>
            {room.temperature && (
              <button
                onClick={() => handleModifyRoomOverriddenTemperature(room.id)}
                className="ml-2 bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded"
              >
                Modify Temperature
              </button>
            )}
          </li>
        ))}
      </ul>
      <button
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        onClick={handleAddToZone}
      >
        Add to Zone
      </button>
      {zones.map((zone) => (
        <div key={zone.id}>
          <strong>{zone.name}</strong>
          <button
            onClick={() => handleModifyTemperature(zone.id)}
            className="ml-2 bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded"
          >
            Modify Temperature
          </button>
          {zone.rooms && (
            <ul>
              {zone.rooms.map((roomName) => (
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
