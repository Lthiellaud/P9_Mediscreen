package com.mediscreen.massessment.controller;

import com.mediscreen.massessment.exception.NotFoundException;
import com.mediscreen.massessment.service.RiskAssessmentService;
import com.mediscreen.massessment.service.implementation.RiskAssessmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RiskAssessmentController.class)
class RiskAssessmentControllerTest {

    @MockBean
    private RiskAssessmentServiceImpl riskAssessmentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRiskAssessment() throws Exception {
        when(riskAssessmentService.assessRiskLevel(1)).thenReturn("None");
        mockMvc.perform(get("/assessment/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("None")))
                .andDo(print());
    }

    @Test
    void getRiskAssessmentException() throws Exception {
        when(riskAssessmentService.assessRiskLevel(1000)).thenThrow(new NotFoundException("Patient 1000 non trouvé"));
        mockMvc.perform(get("/assessment/1000"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}