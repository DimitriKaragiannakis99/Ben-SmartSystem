package com.bensmartsystem.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void testUserConstructor() {
        User user = new User("1", "JohnDoe", "admin");

        assertEquals("1", user.getId());
        assertEquals("JohnDoe", user.getUsername());
        assertEquals("admin", user.getPermissions());
        assertEquals(0, user.getRoomIndex());
    }

    @Test
    void testSetters() {
        User user = new User("1", "JohnDoe", "admin");
        user.setUsername("JaneDoe");
        user.setPermissions("user");
        user.setRoomIndex(1);

        assertEquals("JaneDoe", user.getUsername());
        assertEquals("user", user.getPermissions());
        assertEquals(1, user.getRoomIndex());
    }
}
