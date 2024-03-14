package com.bensmartsystem.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class OutputConsoleTests {
    @Test
    void testSingletonInstance() {
        OutputConsole console1 = OutputConsole.getConsoleLog();
        OutputConsole console2 = OutputConsole.getConsoleLog();

        assertNotNull(console1);
        assertNotNull(console2);
        assertSame(console1, console2, "Both instances should be the same");
    }

    @Test
    void testAddLogAndGetLogList() {
        OutputConsole console = OutputConsole.getConsoleLog();
        console.addLog("Log 1");
        console.addLog("Log 2");

        assertNotNull(console.getLogList());
        assertEquals(2, console.getLogList().size());
        assertEquals("Log 1", console.getLogList().get(0));
        assertEquals("Log 2", console.getLogList().get(1));
    }
}
