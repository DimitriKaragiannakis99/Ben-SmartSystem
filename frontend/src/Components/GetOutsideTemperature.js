import React, { useContext } from 'react';
import { OutsideTemperatureContext } from "./OutsideTemperatureProvider";

function GetOutsideTemperature() {
    const { outsideTemperatures } = useContext(OutsideTemperatureContext);
    
    return (
        <div>
            <h2>Outside Temperatures</h2>
            <ul>
                {outsideTemperatures.map((temperature, index) => (
                    <li key={index}>
                        Date: {temperature.currentDate}, Time: {temperature.currentTime}, Temperature: {temperature.currentTemperature}°C
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default GetOutsideTemperature;
