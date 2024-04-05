import React, { useState } from "react";
import SHC from "../SHC";
import SHS from "../SHS";
import RoomZones from "./RoomZones";
import DashboardSHP from "./DashboardSHP";

const DashboardTabs = () => {
  const [activeTab, setActiveTab] = useState("tab1");

  return (
    <div>
      <ul className="flex border-b">
        <li className="-mb-px mr-1">
          <button
            className={`bg-white inline-block border-l border-t border-r rounded-t py-2 px-4 text-blue-500 font-semibold ${
              activeTab === "tab1" ? "active" : ""
            }`}
            onClick={() => setActiveTab("tab1")}
          >
            SHC
          </button>
        </li>
        <li className="-mb-px mr-1">
          <button
            className={`bg-white inline-block border-l border-t border-r rounded-t py-2 px-4 text-blue-500 font-semibold ${
              activeTab === "tab2" ? "active" : ""
            }`}
            onClick={() => setActiveTab("tab2")}
          >
            SHS
          </button>
        </li>
        <li className="-mb-px mr-1">
          <button
            className={`bg-white inline-block border-l border-t border-r rounded-t py-2 px-4 text-blue-500 font-semibold ${
              activeTab === "tab3" ? "active" : ""
            }`}
            onClick={() => setActiveTab("tab3")}
          >
            SHH
          </button>
        </li>
        <li className="-mb-px mr-1">
          <button
            className={`bg-white inline-block border-l border-t border-r rounded-t py-2 px-4 text-blue-500 font-semibold ${
              activeTab === "tab4" ? "active" : ""
            }`}
            onClick={() => setActiveTab("tab4")}
          >
            SHP
          </button>
        </li>
      </ul>
      <div className="p-4">
        {activeTab === "tab1" && (
          <div>
            <SHC />
          </div>
        )}
        {activeTab === "tab2" && (
          <div>
            <SHS />
          </div>
        )}
        {activeTab === "tab3" && (
          <div>
            <RoomZones />
          </div>
        )}
        {activeTab === "tab4" && (
          <div>
            <DashboardSHP />
          </div>
        )}
      </div>
    </div>
  );
};

export default DashboardTabs;
