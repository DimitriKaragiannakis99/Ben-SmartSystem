package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.User;
import com.bensmartsystem.backend.controller.UserController;
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

    // since test are all run at the same time, getAllUsers() test must be run
    // separately in order for it to pass!!

    @BeforeEach
    void setUp() {
        userController = new UserController();
        RoomController.getRoomList().clear();
        RoomController.getRoomList().add(new Room("Living Room", new ArrayList<>()));
    }

    @Test
    void getUserById() {
        ResponseEntity<?> responseEntity = userController.getUserById("1L");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> userData = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(userData);
        assertEquals("john_doe", userData.get("username"));
    }

    @Test
    void addUser() {
        User newUser = new User("3L", "alice_doe", "{\"useDoors\":true,\"useWindows\":true,\"useLights\":true}");
        userController.addUser(newUser);
        ResponseEntity<?> responseEntity = userController.getUserById("3L");
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

    // test individually!!
    @Test
    void getAllUsers() {
        List<User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void updateUser() {
        User updatedUser = new User("2L", "jane_doe_updated",
                "{\"useDoors\":false,\"useWindows\":true,\"useLights\":true}");
        userController.postMethodName("2L", updatedUser);
        ResponseEntity<?> responseEntity = userController.getUserById("2L");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> userData = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(userData);
        assertEquals("jane_doe_updated", userData.get("username"));
        assertEquals("{\"useDoors\":false,\"useWindows\":true,\"useLights\":true}", userData.get("permissions"));
    }

}
