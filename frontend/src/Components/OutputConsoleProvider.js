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

  const updateConsoleMessages = (newMessages) => {
    setConsoleMessages(newMessages);
  };




  return (
    <OutputConsoleContext.Provider value={{ consoleMessages,updateConsoleMessages }}>
      {children}
    </OutputConsoleContext.Provider>
  );
};

export default OutputConsoleProvider;