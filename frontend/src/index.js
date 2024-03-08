import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import SHC from "./components/SHC";
import RoomEditPage from "./components/EditSimulation";
import UserManagement from "./components/UserManagement";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
     <App />
    // <>
    //     <RoomEditPage />
    //     <SHC />
    //     <UserManagement />
    //     </>
);
