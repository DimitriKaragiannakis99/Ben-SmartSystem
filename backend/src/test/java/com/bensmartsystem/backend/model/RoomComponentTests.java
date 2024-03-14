package com.bensmartsystem.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RoomComponentTests {
    @Test
    void testRoomComponentConstructor() {
        RoomComponent component = new RoomComponent("1", "Light");

        assertEquals("1", component.getId());
        assertEquals("Light", component.getName());
    }

    @Test
    void testSettersAndGetters() {
        RoomComponent component = new RoomComponent();
        component.setId("2");
        component.setName("Window");

        assertEquals("2", component.getId());
        assertEquals("Window", component.getName());
    }
}
