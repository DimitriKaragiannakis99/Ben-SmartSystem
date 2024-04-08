package com.bensmartsystem.backend;
import com.bensmartsystem.backend.controller.HouseController;
import com.bensmartsystem.backend.controller.RoomController;
import com.bensmartsystem.backend.controller.SHH;
public class ConcreteMediator implements Mediator {

    HouseController houseController = null;
    RoomController roomController = null;
    SHH shh = null;

    //Constructor as singleton so we have only one instance of the mediator

    private static ConcreteMediator instance = null;

    private ConcreteMediator() {
        
       
    }

    public static ConcreteMediator getInstance() {
        if(instance == null) {
            instance = new ConcreteMediator();
        }
        return instance;
    }


    public void notify(HouseController h,String event) {
        
        //if our instance is null, we will set it to the instance that is passed in

        if(houseController == null) {
            houseController = h;
        }
        
        if(event.equals("AwayMode")) {
            System.out.println("Away Mode has been toggled");
            //In here we make the Room Controller close all doors and windows 
            RoomController.closeAllDoorsAndWindows();

        }
    }

    public void notify(RoomController r,String event) {
        if(roomController == null) {
            roomController = r;
        }
        if(event.equals("room")) {
            
        }

    }

    public void notify(SHH s,String event) {
        
        if(shh == null) {
            shh = s;
        }
        
        if(event.equals("shh")) {

        }
    }

    
}
