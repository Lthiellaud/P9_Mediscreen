package com.mediscreen.mpatients.controller;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.service.implementation.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
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

    private static final Patient existingPatient = new Patient(1, "Lucas","Ferguson"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");

    private static final Patient patientToBeAdded = new Patient();

    private static final Patient createdPatient = new Patient(1, "New","Patient"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street", "387-866-1399");

    @BeforeEach
    public void initTest() {
        patientToBeAdded.setPatientId(2);
        patientToBeAdded.setFirstName("New");
        patientToBeAdded.setLastName("Patient");
        patientToBeAdded.setBirthDate(LocalDate.of(1980, 1, 1));
        patientToBeAdded.setSex("M");
    }

    @Test
    public void getAllPatientsTest() throws Exception {
        when(patientService.getAllPatient()).thenReturn(Arrays.asList(existingPatient));
        mockMvc.perform(get("/patient/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().string(containsString("Lucas")))
                .andDo(print());
    }

    @Test
    public void getPatientByIdTest() throws Exception {
        when(patientService.getPatientById(1)).thenReturn(existingPatient);
        mockMvc.perform(get("/patient/patientById/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Lucas")))
                .andDo(print());
    }

    @Test
    public void addPatientTest() throws Exception {
        when(patientService.createPatient("New","Patient"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"))
            .thenReturn(createdPatient);
        mockMvc.perform(post("/patient/add").param("family","New")
                .param("given","Patient")
                .param("dob","1980-01-01")
                .param("sex","M")
                .param("address","2 Warren Street ")
                .param("phone","387-866-1399"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("lastName", is("Patient")))
                .andExpect(jsonPath("homeAddress", is("2 Warren Street")))
                .andDo(print());


    }

    @Test
    public void addPatientBadRequestTest() throws Exception {

        mockMvc.perform(post("/patient/add")
                .param("family","New")
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
        RequestBuilder createRequest = MockMvcRequestBuilders
                .put("/patient/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"patientId\": \"1\",\n" +
                        "    \"firstName\": \"New\",\n" +
                        "    \"lastName\": \"Patient\",\n" +
                        "    \"birthDate\": \"1980-21-01\",\n" +
                        "    \"sex\": \"M\",\n" +
                        "    \"homeAddress\" : \"address New\",\n" +
                        "    \"phoneNumber\" : \"333-666-9999\"\n" +
                        "}");

        mockMvc.perform(createRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePatientExceptionTest() throws Exception {
        when(patientService.updatePatient(any(Patient.class)))
                .thenThrow(new AlreadyExistException("Patient existe déjà"));

        RequestBuilder createRequest = MockMvcRequestBuilders
                .put("/patient/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"patientId\": \"1\",\n" +
                        "    \"firstName\": \"New\",\n" +
                        "    \"lastName\": \"Patient\",\n" +
                        "    \"birthDate\": \"1980-01-01\",\n" +
                        "    \"sex\": \"M\",\n" +
                        "    \"homeAddress\" : \"address New\",\n" +
                        "    \"phoneNumber\" : \"333-666-9999\"\n" +
                        "}");

        mockMvc.perform(createRequest)
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    public void updatePatientTest() throws Exception {
        when(patientService.updatePatient(any(Patient.class)))
                .thenReturn(existingPatient);

        RequestBuilder updateRequest = MockMvcRequestBuilders
                .put("/patient/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"patientId\": \"1\",\n" +
                        "    \"firstName\": \"Lucas\",\n" +
                        "    \"lastName\": \"Ferguson\",\n" +
                        "    \"birthDate\": \"1980-01-02\",\n" +
                        "    \"sex\": \"M\",\n" +
                        "    \"homeAddress\" : \"address New\",\n" +
                        "    \"phoneNumber\" : \"333-666-9999\"\n" +
                        "}");

        mockMvc.perform(updateRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("lastName", is("Ferguson")))
                .andDo(print());
    }
}