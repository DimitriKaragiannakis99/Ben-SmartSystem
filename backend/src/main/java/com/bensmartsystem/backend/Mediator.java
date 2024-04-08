package com.bensmartsystem.backend;

import com.bensmartsystem.backend.controller.HouseController;
import com.bensmartsystem.backend.controller.RoomController;
import com.bensmartsystem.backend.controller.SHH;

public interface Mediator {
    
    public void notify(HouseController h,String event);
    public void notify(RoomController r,String event);
    public void notify(SHH s,String event);

}
