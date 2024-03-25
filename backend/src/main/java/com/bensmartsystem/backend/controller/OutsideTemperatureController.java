package com.bensmartsystem.backend.controller;


import com.bensmartsystem.backend.model.OutsideTemperature;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import lombok.Getter;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class OutsideTemperatureController {

    @Getter
    private static final ArrayList<OutsideTemperature> outsideTemperatureList = new ArrayList<>();

    @GetMapping("/getOutsideTemperatures")
    public ResponseEntity<ArrayList<OutsideTemperature>> getOutsideTemperatures() {
        System.out.println(outsideTemperatureList);
      
        return ResponseEntity.ok(outsideTemperatureList);
    }

        
        @PostMapping("/uploadOutsideTemperatures")
        public ResponseEntity<String> uploadOutSideTemperatures(@RequestParam("file") MultipartFile file) {
           
            outsideTemperatureList.clear();
    
            // File was not uploaded correctly
            if (file.isEmpty()) {
                return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
            }
    
            // Read file in and perform tasks:
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                
                while ((line = reader.readLine()) != null) {
                    
                    String[] parts = line.split(",");

                    String curDate = parts[0].trim();

                    String curTime = parts[1].trim();

                    String curTemp = parts[2].trim();
                   
                    outsideTemperatureList.add(new OutsideTemperature(curDate, curTime, curTemp));
                    
                }
                return new ResponseEntity<>("Outside temperatures uploaded successfully", HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>("Error processing file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    
}