package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.controller.SHH;
import com.bensmartsystem.backend.model.Room;
import org.junit.jupiter.api.BeforeAll;
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

    @Test
    public void test_checkTemp_warning() {
        Room room = new Room();
        room.setId("3");
        room.setTemperature(-1);
        rm = new RoomController();
        rm.addRoom(room);

        SHH shh = new SHH();
        ResponseEntity<String> responseEntity = shh.checkTemp();
        assertEquals("Temperature is below zero, pipes may burst!", responseEntity.getBody());
    }

    @Test
    public void test_checkTemp_noWarning() {
        Room room = new Room();
        room.setId("4");
        room.setTemperature(10);
        rm = new RoomController();
        rm.addRoom(room);

        SHH shh = new SHH();
        ResponseEntity<String> responseEntity = shh.checkTemp();
        assertEquals("All room temps look ok!", responseEntity.getBody());
    }

    @Test
    public void testHeating() throws InterruptedException {
        // Create a mock Room object with desired temperature and initial temperature
        Room room = new Room();
        room.setDesiredTemperature(20);
        room.setTemperature(15);
        room.setIsHeaterOn(true);

        // Call the heating method
        SHH.heating(room);

        // Wait for some time to allow the heating method to execute
        Thread.sleep(5000);
        room.setIsHeaterOn(false);
        assertEquals(15.5, room.getTemperature(), 0.01);
    }

    @Test
    public void testCooling() throws InterruptedException {
        Room room = new Room();
        room.setDesiredTemperature(17);
        room.setTemperature(20);
        room.setIsAcOn(true); // simulate turning ac on

        SHH.cooling(room);

        Thread.sleep(5000);
        room.setIsAcOn(false);

        assertEquals(19.5, room.getTemperature(), 0.5);
    }
}

