import React, { useState } from 'react';

const UserSelector = () => {
  const [userType, setUserType] = useState('Parent'); // Default user type

  return (
    <div className="user-selector-container">
      <label htmlFor="user-type-select">Change user type:</label>
      <select
        id="user-type-select"
        value={userType}
        onChange={(e) => setUserType(e.target.value)}
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
