package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeControllerTest {

    private TimeController timeController;

    @BeforeEach
    void setUp() {
        // Initialize TimeController before each test
        timeController = new TimeController();
    }

    @Test
    void getTime_ReturnsInitialTime() {
        // Act
        Time currentTime = timeController.getTime();

        // Assert
        assertEquals(12, currentTime.getHour(), "Hour should be 12");
        assertEquals(1, currentTime.getDay(), "Day should be 1");
        assertEquals(1, currentTime.getMonth(), "Month should be 1");
    }

    @Test
    void setTime_UpdatesTimeSuccessfully() {
        // Arrange
        Time newTime = new Time();
        newTime.setHour(10);
        newTime.setDay(2);
        newTime.setMonth(3);

        // Act
        timeController.setTime(newTime);
        Time updatedTime = timeController.getTime();

        // Assert
        assertEquals(10, updatedTime.getHour(), "Hour should be updated to 10");
        assertEquals(2, updatedTime.getDay(), "Day should be updated to 2");
        assertEquals(3, updatedTime.getMonth(), "Month should be updated to 3");
    }
}

