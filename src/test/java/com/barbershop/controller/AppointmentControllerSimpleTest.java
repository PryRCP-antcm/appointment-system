package com.barbershop.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerSimpleTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void inicioDeberiaRetornarVistaIndex() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"));
    }

    @Test
    void contactoDeberiaRetornarVistaContacto() throws Exception {
        mockMvc.perform(get("/contacto"))
               .andExpect(status().isOk())
               .andExpect(view().name("contacto"));
    }
}
