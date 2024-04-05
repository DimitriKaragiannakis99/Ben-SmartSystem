package com.bensmartsystem.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OutputConsoleTests {
    @Test
    void testSingletonInstance() {
        OutputConsole console1 = OutputConsole.getConsoleLog();
        OutputConsole console2 = OutputConsole.getConsoleLog();

        assertSame(console1, console2, "Both instances should be the same");
    }

    @Test
    void testAddLog() {
        OutputConsole console = OutputConsole.getConsoleLog();

        console.getLogList().clear();

        console.addLog("Test log");

        assertFalse(console.getLogList().isEmpty(), "Log list should not be empty");
        assertEquals("Test log", console.getLogList().get(0), "The log should match the added log");
    }

    @Test
    void testGetLogList() {
        OutputConsole console = OutputConsole.getConsoleLog();
        console.addLog("First log");
        console.addLog("Second log");

        assertEquals("First log", console.getLogList().get(0), "First log should match");
        assertEquals("Second log", console.getLogList().get(1), "Second log should match");
    }
}
