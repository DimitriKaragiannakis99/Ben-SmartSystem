package com.bensmartsystem.backend.model;


public class OutsideTemperature {
    
    private String currentDate;
    private String currentTemperature;
    private String currentTime;

    public OutsideTemperature(String currentDate,String currentTime, String currentTemperature){
        this.currentDate = currentDate;
        this.currentTemperature = currentTemperature;
        this.currentTime = currentTime;

    }

       
        public String getCurrentDate() {
            return currentDate;
        }
    
        public void setCurrentDate(String currentDate) {
            this.currentDate = currentDate;
        }
    
        
        public String getCurrentTemperature() {
            return currentTemperature;
        }
    
        public void setCurrentTemperature(String currentTemperature) {
            this.currentTemperature = currentTemperature;
        }
    
        
        public String getCurrentTime() {
            return currentTime;
        }
    
        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }


}