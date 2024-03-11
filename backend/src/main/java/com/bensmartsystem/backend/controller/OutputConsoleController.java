package com.bensmartsystem.backend.controller;
import org.springframework.web.bind.annotation.*;

import com.bensmartsystem.backend.model.OutputConsole;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class OutputConsoleController {

OutputConsole consoleObj = OutputConsole.getConsoleLog();



@GetMapping("/console")
public List<String> getLog() {
    return consoleObj.getLogList();
}

    
}
