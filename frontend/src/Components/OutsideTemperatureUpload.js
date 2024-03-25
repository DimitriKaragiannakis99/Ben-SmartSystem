import React from 'react'
import axios from "axios";
import { useNavigate } from "react-router-dom";



function OutsideTemperatureUpload() {
    const navigate = useNavigate();
    const handleFileUpload = async (event) => {
       
       
        const file = event.target.files[0];
        console.log("Uploaded file:", file);
    
        const formData = new FormData();
        formData.append("file", file);
    
        
        try {
          const response = await axios.post(
            "http://localhost:8080/api/uploadOutsideTemperatures",
            formData,
            {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            }
          );
    
          // Handle response from backend
          console.log("Response from backend server:", response);
          navigate("/");
          
         
        } catch (error) {
          console.error("Error uploading file:", error);
        }
    };

    const handleUploadButtonClick = () => {
        document.getElementById("fileInput").click(); 
      };

      return (
        <div className="bg-blue-100 h-screen flex justify-center items-center">
          <div className="bg-purple-300 p-6 rounded-lg w-108 h-50 grid place-items-center">
            <p className="text-3xl mb-4">Submit Outside Temperatures </p>
            <input
              type="file"
              accept=".txt"
              onChange={handleFileUpload}
              className="hidden"
              id="fileInput"
            />
            <button
              onClick={handleUploadButtonClick}
              className="bg-purple-400 hover:bg-purple-600 text-white font-bold py-2 px-4 rounded"
            >
              Upload File
            </button>
          </div>
        </div>
      );
}

export default OutsideTemperatureUpload