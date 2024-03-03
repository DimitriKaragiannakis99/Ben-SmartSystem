import React, { useState, useEffect } from "react";
import axios from "axios";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import EditSimulation from "./components/EditSimulation";
import SmartHomeDashboard from "./SmartHomeDashboard";

export default function App() {
  return (
    <div className="bg-white">
      <Router>
        <div>
          <Routes>
            <Route path="/" element={<SmartHomeDashboard />} />
            <Route path="/edit-rooms" element={<EditSimulation />} /> {/* Your room editing component */}
          </Routes>
        </div>
      </Router>
    </div>
  );
}
