package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.House;
import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RoomController {

    @Getter
    private static final ArrayList<Room> roomList = new ArrayList<>();

    // For testing purposes
    public void addRoom(Room room) {
        roomList.add(room);
    }

    @GetMapping("/rooms")
    public ResponseEntity<ArrayList<Room>> getAllRooms() {
        // System.out.println(roomList);
        // Return a new ArrayList to avoid exposing the internal storage structure
        // In here we will assign the users to random rooms for the first time
        updateUsersInRooms();
        return ResponseEntity.ok(roomList);
    }

    @GetMapping("/getAllRooms")
    public ResponseEntity<ArrayList<Room>> getRooms() {
        return ResponseEntity.ok(roomList);
    }

    @PostMapping("/saveRooms")
    public ResponseEntity<String> saveRooms(@RequestBody ArrayList<Room> rms) {
        for (Room incomingRoom : rms) {
            // Find the matching room in the existing list by ID
            for (int i = 0; i < roomList.size(); i++) {
                Room existingRoom = roomList.get(i);
                if (existingRoom.getId().equals(incomingRoom.getId())) {
                    // Update the existing room with the new values
                    existingRoom.updateFrom(incomingRoom);

                    // For each user in the incoming room, update the user's roomID
                    for (String username : incomingRoom.getUsers()) {
                        for (User user : UserController.getUsers()) {
                            if (user.getUsername().equals(username)) {
                                user.setRoomIndex(i);
                            }
                        }
                    }

                    break; // Break out of the loop once the matching room is updated
                }
            }

        }

        // Return a response indicating the operation was successful
        return ResponseEntity.ok("Rooms data saved successfully");
    }

    @PostMapping("/toggleLight")
    public ResponseEntity<?> toggleLight(@RequestParam String roomId) {
        for (Room room : roomList) {
            if (room.getId().equals(roomId) && checkPermissions("lightAccess", roomList.indexOf(room))) {
                room.setIsLightOn(!room.getIsLightOn());
                SimulationEventManager.getInstance().Notify("LightToggled");
                return ResponseEntity.ok(room);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found with id: " + roomId);
    }

    @PostMapping("/toggleWindow")
    public ResponseEntity<?> toggleWindow(@RequestParam String roomId) {
        for (Room room : roomList) {
            if (room.getId().equals(roomId) && checkPermissions("windowAccess", roomList.indexOf(room))) {
                room.setIsWindowOpen(!room.getIsWindowOpen());
                SimulationEventManager.getInstance().Notify("windowToggled");
                return ResponseEntity.ok(room);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found with id: " + roomId);
    }

    @PostMapping("/toggleDoor")
    public ResponseEntity<?> toggleDoor(@RequestParam String roomId) {
        for (Room room : roomList) {
            if (room.getId().equals(roomId) && checkPermissions("doorAccess", roomList.indexOf(room))) {
                room.setIsDoorOpen(!room.getIsDoorOpen());
                SimulationEventManager.getInstance().Notify("doorToggled");
                return ResponseEntity.ok(room);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found with id: " + roomId);
    }

    @PostMapping("/toggleHVAC")
    public ResponseEntity<?> toggleHeater(@RequestParam String roomId) {
        for (Room room : roomList) {
            if (room.getId().equals(roomId) && checkPermissions("shhAccess", roomList.indexOf(room))) {
                room.setIsHVACOn(!room.getIsHVACOn());
                return ResponseEntity.ok(room);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found with id: " + roomId);
    }

    @PostMapping("/toggleHasMotionDetector")
    public ResponseEntity<?> toggleHasMotionDetector(@RequestParam String roomId) {
        for (Room room : roomList) {
            if (room.getId().equals(roomId)) {
                room.setHasMotionDetector(!room.getHasMotionDetector());
                if (room.getHasMotionDetector() == false) {
                    room.setIsMotionDetectorOn(false);
                }
                return ResponseEntity.ok(room);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found with id: " + roomId);
    }

    @PostMapping("/toggleMotionDetector")
    public ResponseEntity<?> toggleMotionDetector(@RequestParam String roomId) {
        for (Room room : roomList) {
            if (room.getId().equals(roomId)) {
                room.setIsMotionDetectorOn(!room.getIsMotionDetectorOn());
                if (room.getIsMotionDetectorOn()) {
                    // Start the timer when the motion detector is turned on
                    HouseController.getHouse().getAlertTimer().startTimer();
                } else {
                    HouseController.getHouse().setPoliceCalled(false);
                }
                return ResponseEntity.ok(room);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found with id: " + roomId);
    }

    @PutMapping("/rooms/{roomId}/temperature")
    public ResponseEntity<?> updateRoomTemperature(@PathVariable String roomId,
            @RequestBody Map<String, Object> payload) {

        Object temperatureObj = payload.get("temperature");
        if (!(temperatureObj instanceof Number)) {
            return ResponseEntity.badRequest().body("Temperature must be a number");
        }

        Double newTemperature;
        if (temperatureObj instanceof Integer) {
            newTemperature = ((Integer) temperatureObj).doubleValue();
        } else {
            newTemperature = (Double) temperatureObj;
        }

        Boolean isOverridden = (Boolean) payload.getOrDefault("overridden", false);

        Room room = findRoomById(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        if (!checkPermissions("shhAccess", roomList.indexOf(room))) {
            SimulationEventManager.getInstance().Notify("InvalidPermission");
            return ResponseEntity.badRequest()
                    .body("You do not have permission to change the temperature in this room");
        }

        room.setDesiredTemperature(newTemperature);
        room.setTemperatureOverridden(isOverridden);
        SimulationEventManager.getInstance().Notify("desiredtemperatureUpdated");
        return ResponseEntity.ok().body("Desired Temperature updated for room with ID: " + roomId);
    }

    // This has the logic for retrieving the .txt file from the front-end, parsing
    // the info and adding it to the hashmap.
    @PostMapping("/uploadRoomLayout")
    public ResponseEntity<String> uploadRoomLayout(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String firstLine = reader.readLine(); // Read the first line for the house name

            if (firstLine != null && firstLine.startsWith("House:")) {
                String houseName = firstLine.substring(6).trim();
                House house = new House("1", houseName); // Create a new House instance
                HouseController.setHouse(house); // Set the new house in HouseController
                RoomController.roomList.clear(); // Clear existing rooms

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length >= 2) {
                        String roomName = parts[0].trim();
                        List<String> components = Arrays.asList(parts[1].trim().split(","));
                        Room newRoom;

                        if (parts.length == 3) {
                            List<String> users = Arrays.asList(parts[2].trim().split(","));
                            newRoom = new Room(roomName, components, users);
                        } else {
                            newRoom = new Room(roomName, components);
                        }
                        house.addRoom(newRoom); // Add the room to the house
                        RoomController.roomList.add(newRoom); // Add the room to the static list
                    } else {
                        return new ResponseEntity<>("Invalid room format in file", HttpStatus.BAD_REQUEST);
                    }
                }
                return new ResponseEntity<>("House and room layout uploaded successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("The first line must start with 'House:' followed by the house name.",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error processing file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method assigns a given user to the first room
    public static void assignUserToFirstRoom(User user) {
        if (!roomList.isEmpty()) {
            roomList.getFirst().addUsers(user.getUsername());
        }
        System.out.println("User added to first room: " + user.getUsername());
        SimulationEventManager.getInstance().Notify("userChangedRoom");
    }

    public static void updateUsersInRooms() {
        // First remove all users from all rooms
        for (Room r : roomList) {
            r.setUsers(new ArrayList<>());
        }

        List<User> users = UserController.getUsers();

        if (users.isEmpty() || roomList.isEmpty()) {
            return;
        }
        for (User u : users) {
            // We will assign the users to random rooms
            // We will use the Random class to generate random numbers
            roomList.get(u.getRoomIndex()).addUsers(u.getUsername());

        }
        SimulationEventManager.getInstance().Notify("usersUpdatedInRooms");
    }

    public static Room findRoomById(String id) {
        for (Room room : roomList) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null; // Or throw an exception if the room is not found
    }

    // Add here the checking of the permissions
    public static boolean checkPermissions(String givenPermission, int targetRoomIndex) {
        // First get the current user's and their permissions
        // Then check if the given permission is in the list of permissions
        // If it is then return true
        // Otherwise return false

        // Get the current user
        User currentUser = UserController.getCurrentUser();

        // Get the current room of the user to check if it needs remote access
        int roomIndex = currentUser.getRoomIndex();
        // Convert that index to a string

        System.out.println("Room index: " + roomIndex + " Target room index: " + targetRoomIndex);
        // Get the permissions of the current user
        String permissions = currentUser.getPermissions();
        // Check if the given permission is in the list of permissions

        Map<String, Object> mapping;
        try {
            mapping = new ObjectMapper().readValue(permissions, HashMap.class);
            boolean needsRemoteAccess = !(roomIndex == targetRoomIndex);
            boolean hasRemoteAccess = ((mapping.containsKey("remoteAccess")
                    && (boolean) mapping.get("remoteAccess") == true));

            // System.out.println("Needs remote access: " + needsRemoteAccess);
            // System.out.println("Has remote access: " + hasRemoteAccess);
            if (mapping.containsKey(givenPermission) && (boolean) mapping.get(givenPermission) == true
                    && (needsRemoteAccess == false || hasRemoteAccess == true)) {

                SimulationEventManager.getInstance().Notify("ValidPermission");
                return true;
            } else {
                SimulationEventManager.getInstance().Notify("InvalidPermission");
                return false;
            }

        } catch (JsonMappingException e) {
            e.printStackTrace();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return false;

    }

}
