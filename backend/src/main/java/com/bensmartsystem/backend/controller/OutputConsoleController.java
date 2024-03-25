package com.bensmartsystem.backend.controller;
import org.springframework.web.bind.annotation.*;

import com.bensmartsystem.backend.model.OutputConsole;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class OutputConsoleController {

OutputConsole consoleObj = OutputConsole.getConsoleLog();



@GetMapping("/console")
public List<String> getLog() {
    return consoleObj.getLogList();
}

@PostMapping("/console/update")
    public void updateLog(@RequestBody List<String> newLogs) {
        

        if (!newLogs.isEmpty()) {
            String lastLog = newLogs.get(newLogs.size() - 1);
            System.out.println("Last log: " + lastLog);

            // Append the last log to log.txt
            try (FileWriter writer = new FileWriter("log.txt", true)) {
                writer.write(lastLog + System.lineSeparator());
            } catch (IOException e) {
                System.err.println("An error occurred while writing to log.txt: " + e.getMessage());
            }

        } else {
            System.out.println("The new logs list is empty.");
        }
       
        
    }

    
}
