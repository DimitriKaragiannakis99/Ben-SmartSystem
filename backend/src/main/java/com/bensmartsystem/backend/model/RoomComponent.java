package com.bensmartsystem.backend.model;

public class RoomComponent {
    
    private String id;
    private String name;
   

    public RoomComponent(){

    }

    public RoomComponent(String id, String name){
        this.id = id;
        this.name = name;
        

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  
    


     

}
