import React, { useState, useEffect } from "react";
import axios from "axios";

function SHC() {
	// state for all room info
	const [rooms, setRooms] = useState([]);

	// state for current simulator user
	const [simulatorUser, setSimulatorUser] = useState("parent");

	// state for selected rooms
	const [selectedRooms, setSelectedRooms] = useState({});

	// state for selected room component
	const [selectedComponent, setSelectedComponent] = useState("");

	// state for console messages
	const [consoleMessages, setConsoleMessages] = useState([]);

	// fetch all room info from the backend on page load
	useEffect(() => {
		axios
			.get("http://localhost:8080/api/rooms")
			.then((response) => {
				setRooms(response.data);
			})
			.catch((error) => {
				console.error("Error fetching room information", error);
			});
	}, []);

	// determine which rooms have been selected (check box)
	const handleRoomCheckChange = (roomName) => {
		setSelectedRooms((prevSelectedRooms) => ({
			...prevSelectedRooms,
			[roomName]: !prevSelectedRooms[roomName],
		}));
	};

	// determine which house component has been selected
	const handleComponentSelection = (componentName) => {
		setSelectedComponent(componentName);
	};

	// select all rooms (all check boxes selected)
	const selectAllRooms = () => {
		const allSelected = rooms.reduce((acc, room) => {
			acc[room.name] = true;
			return acc;
		}, {});
		setSelectedRooms(allSelected);
	};

	// deselect all rooms (all check boxes deselected)
	const deselectAllRooms = () => {
		const noneSelected = rooms.reduce((acc, room) => {
			acc[room.name] = false;
			return acc;
		}, {});
		setSelectedRooms(noneSelected);
	};

	// determines what gets printed to the console based on open/close button press
	const handleOpenCloseAction = (action) => {
		// Check if any room is selected
		const selectedRoomNames = Object.keys(selectedRooms).filter(
			(roomName) => selectedRooms[roomName]
		);

		// Check if a component is selected
		if (!selectedComponent || selectedRoomNames.length === 0) {
			console.log("No component or room selected.");
			return; // Exit the function if no component or room is selected
		}

		const currentTime = new Date().toLocaleTimeString();
		const actionText = action === "open" ? "opened" : "closed";
		const message = `[${currentTime}] [${selectedComponent}] in ${selectedRoomNames.join(
			", "
		)} was ${actionText} by ${simulatorUser} request.`;

		// Add message to the console
		setConsoleMessages((prevMessages) => [...prevMessages, message]);
	};

	return (
		<>
			<div className="container bg-blue-500 mx-auto my-8 p-4">
				<div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
					{/* Container for room components and rooms */}
					<div className="md:col-span-3 md:order-1">
						{/* Box for house components */}
						<div className="mb-4 p-4 border border-gray-200 rounded">
							<h2 className="font-bold mb-3">Room Components</h2>
							<div>
								{rooms.length > 0 && (
									<ul>
										{rooms[0].roomComponents.map((component, index) => (
											<li
												key={index}
												onClick={() => handleComponentSelection(component)}
												style={{
													cursor: "pointer",
													backgroundColor:
														selectedComponent === component
															? "#efefef"
															: "transparent",
													padding: "5px",
													margin: "5px",
													borderRadius: "5px",
												}}
											>
												{component}
											</li>
										))}
									</ul>
								)}
							</div>
							<div>
								<button
									className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
									onClick={() => handleOpenCloseAction("open")}
								>
									Open
								</button>
								<button
									className="px-4 py-2 border border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors"
									onClick={() => handleOpenCloseAction("close")}
								>
									Close
								</button>
							</div>
						</div>

						{/* Box for rooms */}
						<div className="p-4 border border-gray-200 rounded">
							<h2 className="font-bold mb-3">Rooms</h2>
							<ul>
								{rooms.map((room) => (
									<li key={room.id}>
										<label className="flex items-center space-x-2">
											<input
												type="checkbox"
												checked={selectedRooms[room.name] || false}
												onChange={() => handleRoomCheckChange(room.name)}
											/>
											<span>{room.name}</span>
										</label>
									</li>
								))}
							</ul>
							<button
								onClick={selectAllRooms}
								className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-blue-700 transition-colors"
							>
								All
							</button>
							<button
								onClick={deselectAllRooms}
								className="px-4 py-2 border  border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors ml-2"
							>
								None
							</button>
						</div>
					</div>

					{/* Box for the simulation */}
				</div>

				{/* Scrollable console box */}
				<div className="p-4 border border-gray-200 rounded h-48 overflow-auto">
					<h2 className="font-bold mb-3">Output Console</h2>
					{consoleMessages.map((message, index) => (
						<p key={index}>{message}</p>
					))}
				</div>
			</div>
		</>
	);
}

export default SHC;
