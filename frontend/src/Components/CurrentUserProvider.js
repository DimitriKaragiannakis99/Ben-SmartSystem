import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const CurrentUserContext = createContext();

const CurrentUserProvider = ({ children }) => {
  const [currSimUser, setCurrSimUser] = useState(null);



   const updateCurrentSimUser = (newSimUser) =>{
        setCurrSimUser(newSimUser)
   }


  return (
    <CurrentUserContext.Provider value={{ currSimUser,updateCurrentSimUser }}>
      {children}
    </CurrentUserContext.Provider>
  );
};

export default CurrentUserProvider;