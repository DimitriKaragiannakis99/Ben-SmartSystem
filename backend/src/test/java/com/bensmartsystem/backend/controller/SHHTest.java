package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.controller.SHH;
import com.bensmartsystem.backend.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SHHTest {

    private RoomController rm;

    @Test
    public void testHVACOn() {
        // Create a hardcoded room instance
        Room room = new Room();
        room.setId("1");
        room.setTemperature(20); // Current temperature
        room.setDesiredTemperature(22); // Desired temperature
        rm = new RoomController();
        rm.addRoom(room);

        // Create an instance of SHH
        SHH shh = new SHH();

        // Call the method
        ResponseEntity<String> responseEntity = shh.HVAC_on(room.getId());

        // Verify that the heater is turned on and the response is correct
        assertEquals("Heater turned on", responseEntity.getBody());
    }

    @Test
    public void testHVACOff() {
        // Create a hardcoded room instance
        Room room = new Room();
        room.setId("2");
        room.setTemperature(24); // Current temperature (higher than outside temp)
        rm = new RoomController();
        rm.addRoom(room);

        // Create an instance of SHH
        SHH shh = new SHH();

        // Call the method
        ResponseEntity<String> responseEntity = shh.HVAC_off(room.getId());

        // Verify that the HVAC is turned off and the response is correct
        assertEquals("HVAC was turned off", responseEntity.getBody());
    }
}

