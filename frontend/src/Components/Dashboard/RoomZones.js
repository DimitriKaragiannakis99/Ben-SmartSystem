import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { RoomContext } from "./RoomProvider";
import ScheduleTemperatureModal from "./ScheduleTempModal";
import SHH from "../SHH";
import { OutputConsoleContext } from "../OutputConsoleProvider";
import { CurrentUserContext } from "../CurrentUserProvider";

const RoomZones = () => {
  const [rooms, setRooms] = useState([]);
  const [selectedRoomIds, setSelectedRoomIds] = useState([]);
  const [zones, setZones] = useState([]);
  const { updateRoomTemperature } = useContext(RoomContext);
  const [isScheduleModalOpen, setIsScheduleModalOpen] = useState(false);
  const { isSHHOn, setIsSHHOn } = useContext(RoomContext);

  const {currSimUser, updateCurrSimUser} = useContext(CurrentUserContext);
  

  // added OutputConsoleContext
  const { consoleMessages, updateConsoleMessages } =
    useContext(OutputConsoleContext);

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

  const toggleSHH = () => {
    setIsSHHOn(!isSHHOn);
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
    const newTemperature = prompt("Set the new desired temperature:");
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
                const currentDate = new Date().toLocaleDateString();
                const currentTime = new Date().toLocaleTimeString();
                const message = `[${currentDate}][${currentTime}] the desired temperature ${newTemperature}C in rooms of zone ${zone.name} has been changed by  ${currSimUser} `;
                // updating OutputConsole context
                updateConsoleMessages(message);

                return { ...zone, temperature: newTemperature };
              } else {
                return zone;
              }
            })
          );
          alert("Desired Temperature updated successfully.");
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
      !isNaN(newTemperature) &&
      newTemperature >= -40 &&
      newTemperature <= 200
    ) {
      const temperatureAsNumber = parseFloat(newTemperature);

      axios
        .put(`http://localhost:8080/api/rooms/${roomId}/temperature`, {
          temperature: temperatureAsNumber,
          overridden: true,
        })
        .then(() => {
          updateRoomTemperature(roomId, temperatureAsNumber, true);
          const roomName = rooms.find((room) => room.id === roomId)?.name;
          const currentDate = new Date().toLocaleDateString();
          const currentTime = new Date().toLocaleTimeString();
          const message = `[${currentDate}][${currentTime}] the desired temperature ${temperatureAsNumber}C in room ${roomName} has been overridden by ${currSimUser}`;
          // updating OutputConsole context
          updateConsoleMessages(message);

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
        <div className="border-b border-gray-500 pt-4"></div>
        {zones.map((zone) => (
          <div key={zone.id}>
            <strong>{zone.name}</strong>
            <div className="flex">
              <button
                onClick={() => handleModifyTemperature(zone.id)}
                className="ml-2 bg-green-500 hover:bg-green-700 text-white font-bold py-1 px-2 rounded mr-1"
              >
                Modify Temperature
              </button>
              <button
                onClick={() => setIsScheduleModalOpen(true)}
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded mr-1"
              >
                Schedule Temperature
              </button>
              <ScheduleTemperatureModal
                isOpen={isScheduleModalOpen}
                onClose={() => setIsScheduleModalOpen(false)}
                zoneId={zone.id}
              />
            </div>
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
      <div className="flex items-center justify-center">
        <span className="pr-3">Toggle SHH:</span>
        <label className="inline-flex relative items-center cursor-pointer">
          <input
            type="checkbox"
            value={isSHHOn}
            onChange={toggleSHH}
            className="sr-only peer"
          />
          <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-500"></div>
        </label>
      </div>
      <div>
        <SHH />
      </div>
    </div>
  );
};

export default RoomZones;
