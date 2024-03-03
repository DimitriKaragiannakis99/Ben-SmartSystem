import React, { useState, useEffect } from 'react';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import axios from 'axios';

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
    const [users] = useState([
        { id: 'user-1', name: 'User 1' },
        { id: 'user-2', name: 'User 2' },
        // ...and so on
      ]);
      const [rooms, setRooms] = useState([]);

    useEffect(() => {
        // Fetch room information from the backend endpoint using axios
        axios.get('http://localhost:8080/api/rooms')
        .then((response) => {
            setRooms(response.data); // Update the state of rooms with the response from the backend
        })
        .catch((error) => {
            console.error("Error fetching room information", error);
        });
    }, []);
    

      const onDragEnd = (result) => {
        const { source, destination } = result;
      
        // Dropped outside any droppable area
        if (!destination) {
          return;
        }
      
        // Dropped in the same room
        if (source.droppableId === destination.droppableId) {
          const room = rooms.find(r => r.id === source.droppableId);
          const reorderedUsers = reorder(room.users, source.index, destination.index);
          const newRooms = rooms.map(r =>
            r.id === room.id ? { ...r, users: reorderedUsers } : r
          );
          setRooms(newRooms);
        } else {
          // Dropped in a different room
          const sourceRoom = rooms.find(r => r.id === source.droppableId);
          const destRoom = rooms.find(r => r.id === destination.droppableId);
          const sourceUsers = Array.from(sourceRoom.users);
          const destUsers = Array.from(destRoom.users);
          const [movedUser] = sourceUsers.splice(source.index, 1);
          destUsers.splice(destination.index, 0, movedUser);
      
          const newRooms = rooms.map(r => {
            if (r.id === source.droppableId) {
              return { ...r, users: sourceUsers };
            } else if (r.id === destination.droppableId) {
              return { ...r, users: destUsers };
            } else {
              return r;
            }
          });
          setRooms(newRooms);
        }
      };

      const handleSave = () => {
        // Example action: Log the current rooms state to the console
        console.log('Saving current state:', rooms);

        //TODO: Save to backend

        alert('Current state saved! Check the console or local storage.');
    };
      
      

      return (
        <div className='bg-blue-100 min-h-screen'>
        <DragDropContext onDragEnd={onDragEnd}>
          <div className="flex flex-wrap justify-center gap-4 p-4">
            {rooms.map((room, roomIndex) => (
              <Droppable droppableId={room.id} key={room.id}>
                {(provided, snapshot) => (
                  <div
                    ref={provided.innerRef}
                    {...provided.droppableProps}
                    className={`flex flex-col items-center w-96 h-96 border-2 border-dashed border-gray-300 rounded-lg p-2 ${snapshot.isDraggingOver ? 'bg-blue-100' : 'bg-gray-50'}`} 
                  >
                    <h2 className="text-center text-lg font-bold mb-2">{room.name}</h2>
                    <div className="flex-1 w-full"> {/* Container for users */}
                      {room.users.map((userId, userIndex) => {
                        const user = users.find((u) => u.id === userId);
                        return (
                          <Draggable key={user.id} draggableId={user.id} index={userIndex}>
                            {(provided, snapshot) => (
                              <div
                                ref={provided.innerRef}
                                {...provided.draggableProps}
                                {...provided.dragHandleProps}
                                className={`flex items-center justify-center m-1 p-2 w-36 rounded ${snapshot.isDragging ? 'bg-green-200' : 'bg-white'}`}
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