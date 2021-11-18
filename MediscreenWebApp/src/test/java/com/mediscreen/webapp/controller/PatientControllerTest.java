package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.model.Patient;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class)
class PatientControllerTest {

    @MockBean
    private PatientManagementProxy patientManagementProxy;

    @Autowired
    private MockMvc mockMvc;

    private static final Patient existingPatient = new Patient(1, "Lucas","Ferguson"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");

    @Test
    void listPatientTest() throws Exception {
        mockMvc.perform(get("/patient/list"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Disabled
    @Test
    void updatePatient() throws Exception {

        when(patientManagementProxy.updatePatient(any(Patient.class))).thenReturn(existingPatient);
        mockMvc.perform(post("/patient/update/{id}", "1")
                .param("patientId","1")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-15-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andDo(print());

        verify(patientManagementProxy, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    void showUpdatePatientForm() throws Exception {

        when(patientManagementProxy.getPatientById(1)).thenReturn(existingPatient);
        mockMvc.perform(get("/patient/update/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/update"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("patient", existingPatient));

        verify(patientManagementProxy, times(1)).getPatientById(1);
    }

    @Test
    void showUpdatePatientFormNotFoundException() throws Exception {

        when(patientManagementProxy.getPatientById(1)).thenThrow(new NotFoundException("Non trouvé"));
        mockMvc.perform(get("/patient/update/{id}", "1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Non trouvé"));

        verify(patientManagementProxy, times(1)).getPatientById(1);
    }
    @Test
    void showUpdatePatientFormException() throws Exception {

        when(patientManagementProxy.getPatientById(1)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/patient/update/{id}", "1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard"));

        verify(patientManagementProxy, times(1)).getPatientById(1);
    }
}