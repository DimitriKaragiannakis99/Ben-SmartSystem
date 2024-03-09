import React, { useState, useEffect } from 'react';

function DateTimeDisplay() {
    const [currentTime, setCurrentTime] = useState('');

    useEffect(() => {
        fetch('http://localhost:8080/api/time/getTime')
            .then(response => response.json())
            .then(data => {
                const { hour, day, month } = data;
                setCurrentTime(`${hour}:${day}:${month}`);
            })
            .catch(error => {
                console.error('Error fetching time:', error);
            });
    }, []);

    return (
        <div>
            <h1>Current Time: <strong>{currentTime}</strong></h1>
        </div>
    );
}

export default DateTimeDisplay;
