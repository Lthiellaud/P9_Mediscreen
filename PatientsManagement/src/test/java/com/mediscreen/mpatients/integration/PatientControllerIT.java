package com.mediscreen.mpatients.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllPatientsTest() throws Exception {
        mockMvc.perform(get("/patient/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(content().string(containsString("Lucas")))
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

        mockMvc.perform(post("/patient/add").param("family","Ferguson")
                .param("given","Lucas")
                .param("dob","1968-06-22")
                .param("sex","M")
                .param("address","Adresse")
                .param("phone","333-666-1111"))
                .andExpect(status().isConflict())
                .andDo(print());

        assertThat("").isEqualTo("");
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
    public void updatePatientTest() throws Exception {

        RequestBuilder createRequest = MockMvcRequestBuilders
                .put("/patient/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"patientId\": \"12\",\n" +
                        "    \"firstName\": \"New\",\n" +
                        "    \"lastName\": \"Patient\",\n" +
                        "    \"birthDate\": \"1980-01-01\",\n" +
                        "    \"sex\": \"M\",\n" +
                        "    \"homeAddress\" : \"address New\",\n" +
                        "    \"phoneNumber\" : \"333-666-9999\"\n" +
                        "}");

        mockMvc.perform(createRequest)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void deletePatientByIdTest() throws Exception {
       mockMvc.perform(delete("/patient/delete/-1"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


}
