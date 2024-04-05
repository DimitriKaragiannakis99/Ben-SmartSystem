package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.OutsideTemperature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OutsideTemperatureController.class)
public class OutsideTemperatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
public void getOutsideTemperaturesTest() throws Exception {
    mockMvc.perform(get("/api/getOutsideTemperatures")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
}

@Test
public void uploadOutsideTemperaturesTest() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "temperatures.csv",
        "text/csv",
        "2023-03-22,12:00,-5\n2023-03-23,13:00,-3".getBytes()
    );

    mockMvc.perform(multipart("/api/uploadOutsideTemperatures").file(file))
            .andExpect(status().isOk())
            .andExpect(content().string("Outside temperatures uploaded successfully"));
}




}


