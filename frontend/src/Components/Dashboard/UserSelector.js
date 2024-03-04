import React, { useState, useEffect } from 'react';

const UserSelector = () => {
  // initialize userType state with the value from localStorage, otherwise choose Parent
  const [userType, setUserType] = useState(() => {
    const savedUserType = localStorage.getItem('userType');
    return savedUserType || 'Parent';
  });

  const handleUserTypeChange = (e) => {
    const newUserType = e.target.value;
    setUserType(newUserType);
    const timestamp = new Date().toLocaleTimeString();
    localStorage.setItem('userTypeTimestamp', timestamp);
  };
  

  useEffect(() => {
    localStorage.setItem('userType', userType);
  }, [userType]);

  return (
    <div className="user-selector-container">
      <label htmlFor="user-type-select">Change user type:</label>
      <select
        id="user-type-select"
        value={userType}
        onChange={(handleUserTypeChange)}
        className="user-type-dropdown font-bold"
      >
        <option value="Parent">Parent</option>
        <option value="Child">Child</option>
        <option value="Visitor">Visitor</option>
      </select>
    </div>
  );
};

export default UserSelector;
