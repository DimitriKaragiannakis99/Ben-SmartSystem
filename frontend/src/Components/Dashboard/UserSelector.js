import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UserSelector = () => {
  const [currentUser, setCurrentUser] = useState({ id: '', username: '' });

  const fetchCurrentUser = () => {
    const userId = localStorage.getItem('currentUserId');
    if (userId) {
      axios.get(`http://localhost:8080/api/users/${userId}`)
        .then(response => {
          setCurrentUser({
            id: response.data.id,
            username: response.data.username
          });
        })
        .catch(error => console.error("Failed to fetch current user details", error));
    }
  };

  useEffect(() => {
    fetchCurrentUser();
    const intervalId = setInterval(fetchCurrentUser, 5000); // fetch every 5 seconds

    return () => clearInterval(intervalId); // cleanup
  }, []);

  return (
    <div className="user-selector-container">
      <h3>Current User: <strong>{currentUser.username}</strong></h3>
    </div>
  );
};

export default UserSelector;
