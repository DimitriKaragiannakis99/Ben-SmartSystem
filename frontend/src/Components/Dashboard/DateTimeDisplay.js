import React, { useState, useEffect } from "react";

function DateTimeDisplay() {
  const [currentTime, setCurrentTime] = useState("");

  useEffect(() => {
    const fetchTime = () => {
      fetch("http://localhost:8080/api/time/getTime")
        .then((response) => response.json())
        .then((data) => {
          const { hour, minute, second } = data;
          setCurrentTime(`${hour}:${minute}:${second}`);
        })
        .catch((error) => {
          console.error("Error fetching time:", error);
        });
    };

    fetchTime();

    //Update time every second
    const intervalId = setInterval(fetchTime, 1000);
    return () => clearInterval(intervalId); // clear up  on component unmount
  }, []);

  return (
    <div>
      <h1>
        Current Time: <strong>{currentTime}</strong>
      </h1>
    </div>
  );
}

export default DateTimeDisplay;
