import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SmartHomeDashboard from "./SmartHomeDashboard";
import HomeLayout from "./components/HomeLayout";
import EditSimulationPage from "./components/Dashboard/EditSimulationPage";
import SHH from "./components/SHH";

export default function App() {
  return (
    <div className="bg-white">
      <Router>
        <div>
          <Routes>
            <Route path="/" element={<SmartHomeDashboard />} />
            <Route path="/edit-rooms" element={<EditSimulationPage />} />
            <Route path="/home-layout" element={<HomeLayout />} />
            <Route path="/shh" element={<SHH />} />
          </Routes>
        </div>
      </Router>
    </div>
  );
}
