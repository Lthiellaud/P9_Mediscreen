package com.mediscreen.mpatients.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediscreen.mpatients.exception.AlreadyExistException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void addPatientDTOTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(patientService.createPatientDTO(patientToBeAdded)).thenReturn(createdPatient);
        RequestBuilder createRequest = MockMvcRequestBuilders
                .post("/patient/addPatient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patientToBeAdded));

        mockMvc.perform(createRequest)
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void addPatientTest() throws Exception {

        mockMvc.perform(post("/patient/add").param("family","New")
                .param("given","Patient")
                .param("dob","1980-01-01")
                .param("sex","M")
                .param("address","2 Warren Street ")
                .param("phone","387-866-1399"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void addPatientBadRequestTest() throws Exception {

        mockMvc.perform(post("/patient/add").param("family","New")
                .param("given","Patient")
                .param("dob","1980-15-01")
                .param("sex","M")
                .param("address","2 Warren Street ")
                .param("phone","387-866-1399"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void addPatientExistingTest() throws Exception {

        when(patientService.createPatient("New","Patient"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"))
                .thenThrow(new AlreadyExistException("Patient existe déjà"));

        mockMvc.perform(post("/patient/add").param("family","New")
                .param("given","Patient")
                .param("dob","1980-01-01")
                .param("sex","M")
                .param("address","2 Warren Street ")
                .param("phone","387-866-1399"))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    public void updatePatientBadRequestTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        RequestBuilder createRequest = MockMvcRequestBuilders
                .put("/patient/updatePatient/f")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patientToBeAdded));

        mockMvc.perform(createRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePatientTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(patientService.createPatient("New","Patient"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"))
                .thenThrow(new AlreadyExistException("Patient existe déjà"));

        RequestBuilder createRequest = MockMvcRequestBuilders
                .put("/patient/updatePatient/f")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patientToBeAdded));

        mockMvc.perform(createRequest)
                .andExpect(status().isBadRequest());
    }
}