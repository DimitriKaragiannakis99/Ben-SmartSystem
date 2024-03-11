package com.bensmartsystem.backend.model;

import java.util.ArrayList;
import java.util.List;

// Using the Singleton design pattern for OutputConsole class

public class OutputConsole {

    private static OutputConsole consoleLog = new OutputConsole();

    private List<String> logList = new ArrayList<String>();

    private OutputConsole(){
      
    }

    public static OutputConsole getConsoleLog(){
        return consoleLog;
    }

    public List<String> getLogList(){
        return logList;
    }

    public void addLog(String log){
        this.logList.add(log);
    }


        
    
    
}
