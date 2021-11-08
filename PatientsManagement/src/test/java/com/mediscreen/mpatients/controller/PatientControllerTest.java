package com.mediscreen.mpatients.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediscreen.mpatients.model.DTO.PatientDTO;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.service.implementation.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class)
class PatientControllerTest {

    @MockBean
    private PatientServiceImpl patientService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static Patient existingPatient = new Patient(1L, "Lucas","Ferguson"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399", null);

    private static PatientDTO patientToBeAdded = new PatientDTO("New","Patient"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");

    private static Patient createdPatient = new Patient(1L, "New","Patient"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399", null);

    @Test
    public void getAllPatientsTest() throws Exception {
        when(patientService.getAllPatient()).thenReturn(Arrays.asList(existingPatient));
        mockMvc.perform(get("/patient/allPatients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().string(containsString("Lucas")))
                .andDo(print());
    }

    @Test
    public void addPatientTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(patientService.createPatient(patientToBeAdded)).thenReturn(createdPatient);
        RequestBuilder createRequest = MockMvcRequestBuilders
                .post("/patient/addPatient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patientToBeAdded));

        mockMvc.perform(createRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void updatePatientTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        RequestBuilder createRequest = MockMvcRequestBuilders
                .put("/patient/updatePatient?id=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patientToBeAdded));

        mockMvc.perform(createRequest)
                .andExpect(status().isBadRequest());
    }
}