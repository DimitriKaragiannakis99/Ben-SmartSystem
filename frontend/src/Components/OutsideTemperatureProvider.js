import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const OutsideTemperatureContext = createContext();

const OutsideTemperatureProvider = ({ children }) => {
  const [outsideTemperatures, setOutsideTemperatures] = useState([]);


  useEffect(() => {
    axios.get('http://localhost:8080/api/getOutsideTemperatures')
      .then((response) => {
        setOutsideTemperatures(response.data);
      })
      .catch((error) => {
        console.error('Error fetching outside temperatures', error);
      });
  }, []);



  return (
    <OutsideTemperatureContext.Provider value={{ outsideTemperatures }}>
      {children}
    </OutsideTemperatureContext.Provider>
  );
};

export default OutsideTemperatureProvider;