package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeControllerTest {

    private TimeController timeController;
    private Time initialTime;

    @BeforeEach
    void setUp() {
        initialTime = new Time(12, 1, 1);
        timeController = new TimeController(initialTime);
    }

    @Test
    void setTime() {
        Time newTime = new Time(14, 2, 3);
        timeController.setTime(newTime);

        Time currentTime = timeController.getTime();

        assertEquals(newTime.getHour(), currentTime.getHour(), "The hours should match after setting new time.");
        assertEquals(newTime.getDay(), currentTime.getDay(), "The days should match after setting new time.");
        assertEquals(newTime.getMonth(), currentTime.getMonth(), "The months should match after setting new time.");
    }

    @Test
    void getTime() {
        Time currentTime = timeController.getTime();

        assertNotNull(currentTime, "The current time should not be null.");
        assertEquals(initialTime.getHour(), currentTime.getHour(), "The initial hours should match.");
        assertEquals(initialTime.getDay(), currentTime.getDay(), "The initial days should match.");
        assertEquals(initialTime.getMonth(), currentTime.getMonth(), "The initial months should match.");
    }
}
