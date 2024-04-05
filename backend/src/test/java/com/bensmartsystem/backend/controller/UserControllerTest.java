package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        // Reset UserController to a known state
        UserController.getUsers().clear();
        userController = new UserController(); // This will re-add the initial users
        RoomController.getRoomList().clear();
        RoomController.getRoomList().add(new Room("Living Room", new ArrayList<>()));
    }

    @Test
    void getUserById() {
        ResponseEntity<?> responseEntity = userController.getUserById("1L");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> userData = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(userData);
        assertEquals("Parents", userData.get("username")); // Reflect actual initial data
    }

    @Test
    void addUser() {
        User newUser = new User("5L", "alice_doe",
                "{\"remoteAccess\":true,\"doorAccess\":true,\"windowAccess\":true,\"lightAccess\":true,\"shhAccess\":true}");
        userController.addUser(newUser);
        ResponseEntity<?> responseEntity = userController.getUserById("5L");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> userData = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(userData);
        assertEquals("alice_doe", userData.get("username"));
    }

    @Test
    void deleteUser() {
        userController.deleteUser("1L");
        ResponseEntity<?> responseEntity = userController.getUserById("1L");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getAllUsers() {
        List<User> users = userController.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 4); // Reflects initial users plus any added during tests
    }

    @Test
    void updateUser() {
        User updatedUser = new User("2L", "jane_doe_updated",
                "{\"remoteAccess\":true,\"doorAccess\":false,\"windowAccess\":false,\"lightAccess\":false,\"shhAccess\":false}");
        userController.postMethodName("2L", updatedUser);
        ResponseEntity<?> responseEntity = userController.getUserById("2L");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> userData = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(userData);
        assertEquals("jane_doe_updated", userData.get("username"));
        assertEquals(
                "{\"remoteAccess\":true,\"doorAccess\":false,\"windowAccess\":false,\"lightAccess\":false,\"shhAccess\":false}",
                userData.get("permissions"));
    }

    @Test
    void setCurrentUserAndGet() {
        userController.setCurrentUser("3L");
        ResponseEntity<?> responseEntity = userController.getCurrentUserName();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Guests", responseEntity.getBody()); // Reflect actual initial data
    }
}
