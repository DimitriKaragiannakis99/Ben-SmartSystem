import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import EditSimulation from "./components/EditSimulationComponent";
import SmartHomeDashboard from "./SmartHomeDashboard";
import HomeLayout from "./components/HomeLayout";
import EditSimulationPage from "./components/Dashboard/EditSimulationPage";
import OutsideTemperatureUpload from "./components/OutsideTemperatureUpload";
import GetOutsideTemperature from "./components/GetOutsideTemperature";
import OutsideTemperatureProvider from './components/OutsideTemperatureProvider'


export default function App() {
  return (
    <div className="bg-white">
      <OutsideTemperatureProvider>
      <Router>
        <div>
          <Routes>
            <Route path="/" element={<SmartHomeDashboard />} />
            <Route path="/edit-rooms" element={<EditSimulationPage />} />
            <Route path="/home-layout" element={<HomeLayout />} />
            <Route path="/uploadTemps" element={<OutsideTemperatureUpload  />} />
            <Route path="/getTemps" element={<GetOutsideTemperature  />} />
          </Routes>
        </div>
      </Router>
      </OutsideTemperatureProvider>
    </div>
  );
}
