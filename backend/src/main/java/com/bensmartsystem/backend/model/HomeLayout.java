package com.bensmartsystem.backend.model;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

//FOR NOW THIS CLASS IS USELESS SINCE ITS FUNCTIONALITY IS IN ROOMCONTROLLER
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/home-layout")
public class HomeLayout {

    // Static list to hold rooms
    public static List<Room> roomsList = new ArrayList<>();

    //Method to create rooms based on file input:
    public static List<Room> createRooms(StringBuilder content) {
        List<Room> rooms = new ArrayList<>();

        // Split the content by newline character to process each line
        String[] lines = content.toString().split("\\n");

        for (String line : lines) {
            // Split the line by a delimiter to separate room name and components
            String[] parts = line.split(":");

            // Check if the line has both room name and components
            if (parts.length == 2) {
                String roomName = parts[0].trim();
                List<String> components = Arrays.asList(parts[1].trim().split(","));

                // Create a new Room instance and add it to the list
                rooms.add(new Room(roomName, components));
            }
        }
        return rooms;
    }

    //Any POST request to /home-layout/upload will be handled by this method
    @PostMapping("/upload")
    public ResponseEntity<List<Room>> uploadFile(@RequestParam("file") MultipartFile file) {
        //File was not uploaded correctly
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Read file in and perform tasks:
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            StringBuilder content = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null){
                content.append(line).append("\n");
            }

            //Create a rooms list to hold info
            List<Room> rooms = createRooms(content);

            //Assign rooms to static list to pass to room controller file:
            roomsList = rooms;

            //Proof we created the rooms properly:
//            for(Room r : rooms){
//                System.out.println(r);
//            }

            //Return a success status response + new room objects
            return new ResponseEntity<>(rooms, HttpStatus.OK);

        }catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally {
            System.out.println(roomsList.size());
        }

    }
}

