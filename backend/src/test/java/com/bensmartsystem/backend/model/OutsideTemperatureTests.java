package com.bensmartsystem.backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OutsideTemperatureTests {

    @Test
    void testObjectCreation() {
        OutsideTemperature temp = new OutsideTemperature("2023-03-22", "12:00", "-5");
        assertNotNull(temp, "The OutsideTemperature object should not be null");
    }

    @Test
    void testGetters() {
        String currentDate = "2023-03-22";
        String currentTime = "12:00";
        String currentTemperature = "-5";
        OutsideTemperature temp = new OutsideTemperature(currentDate, currentTime, currentTemperature);
        
        assertEquals(currentDate, temp.getCurrentDate(), "The date should match the constructor argument");
        assertEquals(currentTime, temp.getCurrentTime(), "The time should match the constructor argument");
        assertEquals(currentTemperature, temp.getCurrentTemperature(), "The temperature should match the constructor argument");
    }

    @Test
    void testSetters() {
        OutsideTemperature temp = new OutsideTemperature("2023-03-22", "12:00", "-5");
        
        String newDate = "2023-03-23";
        String newTime = "13:00";
        String newTemperature = "-3";
        
        temp.setCurrentDate(newDate);
        temp.setCurrentTime(newTime);
        temp.setCurrentTemperature(newTemperature);
        
        assertEquals(newDate, temp.getCurrentDate(), "The date should be updated");
        assertEquals(newTime, temp.getCurrentTime(), "The time should be updated");
        assertEquals(newTemperature, temp.getCurrentTemperature(), "The temperature should be updated");
    }
}