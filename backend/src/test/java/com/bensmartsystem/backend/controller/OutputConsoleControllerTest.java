package com.bensmartsystem.backend.controller;
import com.bensmartsystem.backend.model.OutputConsole;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OutputConsoleControllerTest {

    private OutputConsoleController outputConsoleController;

    @BeforeEach
    void setUp() {
        outputConsoleController = new OutputConsoleController();
        
       
    }

    @Test
    void getLog_ReturnsEmptyListIfNoLogs() {
       
        List<String> log = outputConsoleController.getLog();
        
       
        assertTrue(log.isEmpty(), "Log should be empty when no messages are logged.");
    }

    @Test
    void getLog_ReturnsMessagesWhenLogged() {
        
        String message = "Test log message";
        OutputConsole.getConsoleLog().addLog(message); // Adding a log message for the test
        
        
        List<String> log = outputConsoleController.getLog();
        
       
        assertEquals(1, log.size(), "Log should contain one message.");
        assertEquals(message, log.get(0), "The log should contain the test message.");
    }
}
