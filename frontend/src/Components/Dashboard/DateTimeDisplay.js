import React, { useState, useEffect } from 'react';

const DateTimeDisplay = () => {
  const [currentDate, setCurrentDate] = useState(new Date());

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentDate(new Date());
    }, 1000); // Update the date every second

    return () => {
      clearInterval(timer); // Clear the interval on component unmount
    };
  }, []);

  return (
    <div>
      <p>{currentDate.toLocaleString()}</p>
    </div>
  );
};

export default DateTimeDisplay;