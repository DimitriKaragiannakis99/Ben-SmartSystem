package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.House;
import com.bensmartsystem.backend.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HouseController.class)
class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private House house;

    private List<Room> rooms;

    @BeforeEach
    void setUp() {
        rooms = new ArrayList<>();
        rooms.add(new Room("Room1", new ArrayList<>())); // Assuming there's a suitable constructor in Room class
    }

    @Test
    void createHouse() throws Exception {
        mockMvc.perform(post("/api/house/create")
                .param("name", "MyHouse")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"name\":\"Room1\",\"roomComponents\":[]}]")) // Assuming Room class has name and roomComponents fields
                .andExpect(status().isOk())
                .andExpect(content().string("House created successfully with name: MyHouse"));
    }

    @Test
    void getAwayModeWhenHouseNotCreated() throws Exception {
        mockMvc.perform(get("/api/house/awayMode"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("House has not been created"));
    }

    @Test
    void getAwayModeWhenHouseCreated() throws Exception {
        when(house.isAwayModeOn()).thenReturn(true);
        HouseController.setHouse(house);

        mockMvc.perform(get("/api/house/awayMode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAwayModeOn", is(true)));
    }

    @Test
    void toggleAwayModeWhenHouseCreated() throws Exception {
        when(house.isAwayModeOn()).thenReturn(false, true);
        HouseController.setHouse(house);

        mockMvc.perform(post("/api/house/toggleAwayMode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAwayModeOn", is(true)));
    }
}

