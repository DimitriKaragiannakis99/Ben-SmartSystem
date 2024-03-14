package com.bensmartsystem.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TimeTests {
    @Test
    void testTimeConstructor() {
        Time time = new Time(10, 15, 6);

        assertEquals(10, time.getHour());
        assertEquals(15, time.getDay());
        assertEquals(6, time.getMonth());
    }

    @Test
    void testDefaultConstructor() {
        Time time = new Time();

        assertEquals(0, time.getHour());
        assertEquals(0, time.getDay());
        assertEquals(0, time.getMonth());
    }

    @Test
    void testSetters() {
        Time time = new Time();
        time.setHour(23);
        time.setDay(31);
        time.setMonth(12);

        assertEquals(23, time.getHour());
        assertEquals(31, time.getDay());
        assertEquals(12, time.getMonth());
    }
}
