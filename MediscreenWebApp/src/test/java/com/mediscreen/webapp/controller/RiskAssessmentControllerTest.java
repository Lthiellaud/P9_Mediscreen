package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.model.Patient;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import com.mediscreen.webapp.proxies.RiskAssessmentProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RiskAssessmentController.class)
class RiskAssessmentControllerTest {

    @MockBean
    private PatientManagementProxy patientManagementProxy;

    @MockBean
    private RiskAssessmentProxy riskAssessmentProxy;

    @Autowired
    private MockMvc mockMvc;

    private static final Patient patientToAssess = new Patient(1, "Lucas","Ferguson"
            , LocalDate.now().minusYears(30), "M", "2 Warren Street ", "387-866-1399");


    @Test
    void showPatientAssessment() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(riskAssessmentProxy.getRiskAssessment(1)).thenReturn("None");

        mockMvc.perform(get("/assessment/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("assessment/get"))
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString("(30 ans)")))
                .andDo(print());

    }

    @Test
    void showPatientAssessmentNotFound() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenThrow(new NotFoundException("patient non trouvé"));
        when(riskAssessmentProxy.getRiskAssessment(1)).thenReturn("None");

        mockMvc.perform(get("/assessment/1"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/patient/list"))
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "patient non trouvé"))
                .andDo(print());

    }

    @Test
    void showPatientAssessmentException() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenThrow(new RuntimeException("patient non trouvé"));
        when(riskAssessmentProxy.getRiskAssessment(1)).thenReturn("None");

        mockMvc.perform(get("/assessment/1"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/patient/list"))
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard"))
                .andDo(print());

    }
}