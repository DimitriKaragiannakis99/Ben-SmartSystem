import React, { useState, useEffect } from "react";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import axios from "axios";

// A little function to help us with reordering the result
const reorder = (list, startIndex, endIndex) => {
  const result = Array.from(list);
  const [removed] = result.splice(startIndex, 1);
  result.splice(endIndex, 0, removed);
  return result;
};

function UserFigure({ name }) {
  return (
    <div className="text-center m-2.5">
      <div>{name}</div>
      <img src="/images/stickman.png" alt="User" className="w-12 h-auto" />
    </div>
  );
}

const RoomEditPage = () => {
  const [users, setUsers] = useState([]);

  const initialObjects = [
    { id: "object-1", content: "Special Object", placedInWindow: null },
  ];
  const [objects, setObjects] = useState(initialObjects);
  const [rooms, setRooms] = useState([]);
  const [windowBlocked, setWindowBlocked] = useState({});

  useEffect(() => {
    // Fetch room information from the backend endpoint using axios
    axios
      .get("http://localhost:8080/api/rooms")
      .then((response) => {
        setRooms(response.data); // Update the state of rooms with the response from the backend
        const initialWindowBlocked = response.data.reduce(
          (acc, room) => ({
            ...acc,
            [`${room.id}-window`]: room.isWindowBlocked,
          }),
          {}
        );
        setWindowBlocked(initialWindowBlocked);

        setObjects(initializeObjects(response.data)); // Initialize the objects with the response from the backend

        // Process and initialize users based on the room data
        const usersFromRooms = response.data.flatMap((room) =>
          room.users.map((userId) => ({
            id: userId,
            name: `User ${userId.split("-")[1]}`,
            room: room.id,
          }))
        );
        setUsers(usersFromRooms);
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  }, []);

  // Assuming each blocked window should have an associated object
  const initializeObjects = (rooms) => {
    // Create an object for each room, initially unassigned
    return rooms.map((room, index) => ({
      id: `object-${index + 1}`, // Unique ID for each object
      content: "Special Object", // You might customize this per object if needed
      placedInWindow: room.isWindowBlocked ? room.id + "-window" : null,
    }));
  };

  const unblockWindow = (windowId) => {
    // Update windowBlocked state to mark the window as not blocked
    setWindowBlocked((prev) => ({ ...prev, [windowId]: false }));

    const updatedRooms = rooms.map((room) => {
      if (room.id + "-window" === windowId) {
        return { ...room, isWindowBlocked: false };
      }
      return room;
    });
    setRooms(updatedRooms);

    // Optionally, update the objects state to remove the object from the window
    const updatedObjects = objects.map((obj) => {
      if (obj.placedInWindow === windowId) {
        return { ...obj, placedInWindow: null };
      }
      return obj;
    });
    setObjects(updatedObjects);
  };

  const onDragEnd = (result) => {
    const { source, destination } = result;

    // Dropped outside any droppable area
    if (!destination) {
      return;
    }

    // Dropped in the same room
    if (
      source.droppableId.startsWith("room") &&
      destination.droppableId.startsWith("room")
    ) {
      // Ensure we're only dealing with user movements, not window drops
      if (!destination.droppableId.includes("-window")) {
        let newRooms = [...rooms];
        const sourceRoomIndex = rooms.findIndex(
          (room) => room.id === source.droppableId
        );
        const destRoomIndex = rooms.findIndex(
          (room) => room.id === destination.droppableId
        );
        const draggedUser = newRooms[sourceRoomIndex].users.splice(
          source.index,
          1
        )[0];

        // Check if user is dropped in the same room or a different one
        if (source.droppableId === destination.droppableId) {
          newRooms[sourceRoomIndex].users.splice(
            destination.index,
            0,
            draggedUser
          );
        } else {
          newRooms[destRoomIndex].users.splice(
            destination.index,
            0,
            draggedUser
          );
        }

        setRooms(newRooms);
      }
    }
    if (
      source.droppableId === "objects-area" &&
      destination.droppableId.includes("-window")
    ) {
      const windowId = destination.droppableId;

      // Update windowBlocked state to mark the window as blocked
      setWindowBlocked((prev) => ({ ...prev, [windowId]: true }));

      // Optionally, update the objects state to reflect the dropped object
      const updatedObjects = objects.map((obj) => {
        if (obj.id === source.draggableId) {
          return { ...obj, placedInWindow: windowId };
        }
        return obj;
      });
      setObjects(updatedObjects);

      // Update the corresponding room's isBlocked property to true
      const roomId = windowId.replace("-window", "");
      const updatedRooms = rooms.map((room) => {
        if (room.id === roomId) {
          return { ...room, isWindowBlocked: true };
        }
        return room;
      });

      // Update the state of rooms
      setRooms(updatedRooms);
    }
  };

  const handleSave = () => {
    // Example action: Log the current rooms state to the console
    console.log("Saving current state:", rooms);

    // Prepare the data to be sent to the backend
    const dataToSend = rooms.map((room) => ({
      id: room.id,
      name: room.name,
      users: room.users,
      isBlocked: room.isBlocked, // Assuming this property indicates whether the window is blocked or not
    }));

    // Send a POST request to the backend API endpoint
    axios
      .post("http://localhost:8080/api/saveRooms", dataToSend)
      .then((response) => {
        // Handle success
        console.log("Rooms saved successfully:", response.data);
        alert("Rooms saved successfully!");
      })
      .catch((error) => {
        // Handle error
        console.error("Error saving rooms:", error);
        alert("Failed to save rooms. Please try again later.");
      });
  };

  return (
    <div className="bg-blue-100 min-h-screen">
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="objects-area" isDropDisabled={true}>
          {(provided) => (
            <div
              ref={provided.innerRef}
              {...provided.droppableProps}
              className="objects-area p-2 m-1 bg-gray-100 rounded-lg flex flex-col items-center justify-center"
              style={{ maxWidth: "200px" }}
            >
              <Draggable
                key="unique-object"
                draggableId="unique-object"
                index={0}
              >
                {(provided) => (
                  <div
                    className="bg-blue-500 p-2 rounded shadow text-white cursor-grab"
                    ref={provided.innerRef}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    style={{ ...provided.draggableProps.style, cursor: "grab" }}
                  >
                    Object
                  </div>
                )}
              </Draggable>
              {provided.placeholder}
            </div>
          )}
        </Droppable>
        <div className="flex flex-wrap justify-center gap-4 p-4">
          {rooms.map((room, roomIndex) => (
            <div key={room.id} className="flex">
              <Droppable droppableId={room.id} key={room.id}>
                {(provided, snapshot) => (
                  <div
                    ref={provided.innerRef}
                    {...provided.droppableProps}
                    className={`flex flex-col items-center w-96 h-96 border-2 border-dashed border-gray-300 rounded-lg p-2 ${
                      snapshot.isDraggingOver ? "bg-blue-100" : "bg-gray-50"
                    }`}
                  >
                    <h2 className="text-center text-lg font-bold mb-2">
                      {room.name}
                    </h2>
                    <div className="flex-1 w-full">
                      {" "}
                      {/* Container for users */}
                      {room.users.map((userId, userIndex) => {
                        const user = users.find((u) => u.id === userId);
                        return (
                          <Draggable
                            key={user.id}
                            draggableId={user.id}
                            index={userIndex}
                          >
                            {(provided, snapshot) => (
                              <div
                                ref={provided.innerRef}
                                {...provided.draggableProps}
                                {...provided.dragHandleProps}
                                className={`flex items-center justify-center m-1 p-2 w-36 rounded ${
                                  snapshot.isDragging
                                    ? "bg-green-200"
                                    : "bg-white"
                                }`}
                              >
                                <UserFigure name={user.name} />
                              </div>
                            )}
                          </Draggable>
                        );
                      })}
                      {provided.placeholder}
                    </div>
                  </div>
                )}
              </Droppable>
              {/* Window Droppable */}
              <Droppable droppableId={`${room.id}-window`}>
                {(provided, snapshot) => (
                  <div
                    ref={provided.innerRef}
                    {...provided.droppableProps}
                    className={`w-32 h-32 border-2 border-dashed border-gray-300 rounded-lg p-2 pr-2 flex flex-col items-center justify-center ${
                      snapshot.isDraggingOver ? "bg-blue-200" : "bg-gray-50"
                    }`}
                    style={{ minHeight: "100px", minWidth: "100px" }}
                  >
                    <h3 className="font-bold">Window</h3>
                    {windowBlocked[`${room.id}-window`] && (
                      <div className="text-red-500 text-center">
                        Window Blocked
                        <button
                          onClick={() => unblockWindow(`${room.id}-window`)}
                          className="ml-2 bg-transparent text-red-500 font-semibold"
                        >
                          x
                        </button>
                      </div>
                    )}
                    {provided.placeholder}
                  </div>
                )}
              </Droppable>
            </div>
          ))}
        </div>
      </DragDropContext>
      <button
        className="mt-4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        onClick={handleSave}
      >
        Update Simulation
      </button>
    </div>
  );
};

export default RoomEditPage;
