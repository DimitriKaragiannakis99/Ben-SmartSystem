import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const OutputConsoleContext = createContext();

const OutputConsoleProvider = ({ children }) => {
  const [consoleMessages, setConsoleMessages] = useState([]);


  useEffect(() => {
    axios
      .get("http://localhost:8080/api/console")
      .then((response) => {
        setConsoleMessages(response.data);
      })
      .catch((error) => {
        console.error("Error fetching room information", error);
      });
  }, []);

  const updateConsoleMessages = (newMessage) => {
    setConsoleMessages(prevMessages => {
        const updatedMessages = [...prevMessages, newMessage];

        // Optionally, log messages here if needed
        updatedMessages.forEach((message, index) => {
            console.log(`Message ${index}:`, message);
        });

        
         axios.post("http://localhost:8080/api/console/update", updatedMessages, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            console.log("Console messages updated successfully:", response.data);
        })
        .catch(error => {
            console.error("Error updating console messages:", error);
        }); 

        return updatedMessages; 
    });
};





  return (
    <OutputConsoleContext.Provider value={{ consoleMessages,updateConsoleMessages }}>
      {children}
    </OutputConsoleContext.Provider>
  );
};

export default OutputConsoleProvider;