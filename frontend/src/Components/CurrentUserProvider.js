import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const CurrentUserContext = createContext();

const CurrentUserProvider = ({ children }) => {
  const [currentSimUser, setCurrentSimUser] = useState(null);



   const updateCurrentSimUser = (newSimUser) =>{
        setCurrentSimUser(newSimUser)
   }


  return (
    <CurrentUserContext.Provider value={{ currentSimUser,updateCurrentSimUser }}>
      {children}
    </CurrentUserContext.Provider>
  );
};

export default CurrentUserProvider;