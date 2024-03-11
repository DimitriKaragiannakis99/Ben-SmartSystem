import React, { useState } from "react";
import HomeLayout from "../HomeLayout";
import RoomEditPage from "../EditSimulationComponent";

const EditSimulationPage = () => {
  const [simulationData, setSimulationData] = useState(null);

  // Callback function to handle submission from HomeLayout
  const handleSubmission = (data) => {
    setSimulationData(data);
  };

  return (
    <div>
      {simulationData ? (
        <RoomEditPage simulationData={simulationData} />
      ) : (
        <HomeLayout onSubmission={handleSubmission} />
      )}
    </div>
  );
};

export default EditSimulationPage;
