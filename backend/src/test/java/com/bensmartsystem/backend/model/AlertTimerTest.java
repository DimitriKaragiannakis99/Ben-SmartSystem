package com.bensmartsystem.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertTimerTest {
    private AlertTimer alertTimer;
    private House house;

    @BeforeEach
    void setUp() {
        house = new House("MyHouse", "house123");
        alertTimer = new AlertTimer(5, house);
    }

    @Test
    void testStartTimer() throws InterruptedException {
        assertFalse(house.isPoliceCalled(), "Police should not be called initially");
        alertTimer.startTimer();
        Thread.sleep(6000); // Wait for the timer to expire
        assertTrue(house.isPoliceCalled(), "Police should be called after timer expires");
    }

    @Test
    void testGetDelayInSeconds() {
        assertEquals(5, alertTimer.getDelayInSeconds(), "Delay should be 5 seconds");
    }

    @Test
    void testSetDelayInSeconds() {
        alertTimer.setDelayInSeconds(10);
        assertEquals(10, alertTimer.getDelayInSeconds(), "Delay should be updated to 10 seconds");
    }
}
