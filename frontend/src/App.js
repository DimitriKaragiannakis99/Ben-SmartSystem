import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import EditSimulation from "./components/EditSimulation";
import SmartHomeDashboard from "./SmartHomeDashboard";
import HomeLayout from "./components/HomeLayout";

export default function App() {
  return (
    <div className="bg-white">
      <Router>
        <div>
          <Routes>
            <Route path="/" element={<SmartHomeDashboard />} />
            <Route path="/edit-rooms" element={<EditSimulation />} />
            <Route path="/home-layout" element={<HomeLayout />} />
          </Routes>
        </div>
      </Router>
    </div>
  );
}
