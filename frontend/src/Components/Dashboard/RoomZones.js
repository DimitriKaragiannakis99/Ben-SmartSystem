import React, { useState, useEffect } from "react";
import axios from "axios";

const RoomZones = () => {
  const [rooms, setRooms] = useState([]);
  const [selectedRooms, setSelectedRooms] = useState([]);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [zoneName, setZoneName] = useState("");

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/rooms")
      .then((response) => {
        setRooms(response.data.map((room) => ({ ...room })));
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  }, []);

  const handleCheckboxChange = (roomId) => {
    setSelectedRooms((prevSelectedRooms) =>
      prevSelectedRooms.includes(roomId)
        ? prevSelectedRooms.filter((id) => id !== roomId)
        : [...prevSelectedRooms, roomId]
    );
  };

  const handleAddToZone = () => {
    if (selectedRooms.length === 0) {
      alert("Please select at least one room.");
      return;
    }
    const zoneName = prompt("Enter a zone name:");
    if (zoneName) {
      // Construct the Zone object with the name and the list of Room objects
      const zone = {
        name: zoneName,
        rooms: selectedRooms, // Ensure this matches the structure expected by your backend
      };

      console.log(zone);

      // Use Axios to send a POST request
      axios
        .post("http://localhost:8080/api/zones", zone)
        .then((response) => {
          console.log("Success:", response.data);
          // Handle success (e.g., showing a success message, clearing selections)
          setIsPopupOpen(false);
          setZoneName("");
          setSelectedRooms([]); // Optionally clear selected rooms if necessary
        })
        .catch((error) => {
          console.error("Error:", error);
          // Handle error (e.g., showing an error message)
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
                checked={selectedRooms.includes(room.id)}
                onChange={() => handleCheckboxChange(room.id)}
              />
              {room.name}
            </label>
          </li>
        ))}
      </ul>
      <button onClick={handleAddToZone}>Add to Zone</button>
    </div>
  );
};

export default RoomZones;
