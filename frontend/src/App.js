import React, { useState, useEffect } from "react";
import axios from "axios";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import EditSimulation from "./components/EditSimulation";

export default function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    //Fetch user information from backend endpoint using axios
    axios
      .get("http://localhost:8080/api/users/1")
      .then((response) => {
        setUser(response.data); //Were updating the state of user with the response from backend
      })
      .catch((error) => {
        console.log("Error fetching user information", error);
      });
  }, []); //Empty dependency array -> effect runs only once

  return (
    <div className="bg-blue-100">

      <Router>
        <div>
          <Routes>
            <Route path="/" element={<div className="flex justify-center items-center h-screen">
        <div className="bg-purple-300 p-6 rounded-lg w-108 h-50">
          <p className="text-5xl mb-4">User Information</p>
          {user ? (
            <div className="text-xl">
              <p>
                <strong>ID: </strong>
                {user.id}
              </p>
              <p>
                <strong>Username: </strong> {user.username}
              </p>
              <p>
                <strong>Email: </strong>
                {user.email}
              </p>
            </div>
          ) : (
            <p>Loading...</p>
          )}
        </div>
      </div>} />
            <Route path="/edit-rooms" element={<EditSimulation />} /> {/* Your room editing component */}
          </Routes>
        </div>
      </Router>
    </div>
  );
}
