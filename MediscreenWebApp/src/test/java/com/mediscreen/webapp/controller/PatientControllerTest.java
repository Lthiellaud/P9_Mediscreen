package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.AlreadyExistException;
import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.model.Patient;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
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

    @Test
    void updatePatient() throws Exception {

        when(patientManagementProxy.updatePatient(any(Patient.class))).thenReturn(existingPatient);
        mockMvc.perform(post("/patient/update/{id}", "1")
                .param("patientId","1")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().is(302))
                .andExpect(flash().attribute("messageType", "success"))
                .andExpect(flash().attribute("message", "Patient mis à jour"))
                .andExpect(redirectedUrl("/patient/list"))
                .andDo(print());

        verify(patientManagementProxy, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    void updatePatientErreurFormulaire() throws Exception {

        when(patientManagementProxy.updatePatient(any(Patient.class))).thenReturn(existingPatient);
        mockMvc.perform(post("/patient/update/{id}", "1")
                .param("patientId","1")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","2080-12-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patient", "birthDate", "Past"))
                .andExpect(view().name("patient/update"))
                .andDo(print());

        verify(patientManagementProxy, times(0)).updatePatient(any(Patient.class));
    }

    @Test
    void updatePatientErreurFormulaire2() throws Exception {

        when(patientManagementProxy.updatePatient(any(Patient.class))).thenReturn(existingPatient);
        mockMvc.perform(post("/patient/update/{id}", "1")
                .param("patientId","1")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1080-15-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patient", "birthDate", "typeMismatch"))
                .andExpect(view().name("patient/update"))
                .andDo(print());

        verify(patientManagementProxy, times(0)).updatePatient(any(Patient.class));
    }

    @Test
    void updatePatientNotFound() throws Exception {

        when(patientManagementProxy.updatePatient(any(Patient.class))).thenThrow(new NotFoundException("patientId 1 non trouvé"));
        mockMvc.perform(post("/patient/update/{id}", "1")
                .param("patientId","1")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-12-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().is(302))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "patientId 1 non trouvé"))
                .andExpect(redirectedUrl("/patient/list"))
                .andDo(print());

        verify(patientManagementProxy, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    void updatePatientOtherException() throws Exception {

        when(patientManagementProxy.updatePatient(any(Patient.class))).thenThrow(new RuntimeException("Autre problème"));
        mockMvc.perform(post("/patient/update/{id}", "1")
                .param("patientId","1")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-12-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().is(302))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème pendant la mise à jour, réessayer plus tard"))
                .andExpect(redirectedUrl("/patient/list"))
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
                .andExpect(flash().attribute("messageType", "error"))
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
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard"));

        verify(patientManagementProxy, times(1)).getPatientById(1);
    }

    @Test
    public void getPatientAdd() throws Exception {
        mockMvc.perform(get("/patient/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/add"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void postPatientValidate() throws Exception {
        when(patientManagementProxy.addPatient("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"))
                .thenReturn(existingPatient);
        mockMvc.perform(post("/patient/validate")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/add"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("messageType", "success"))
                .andExpect(model().attribute("message", "Patient id 1 créé"));
    }

    @Test
    public void postPatientValidateExistingPatient() throws Exception {
        when(patientManagementProxy.addPatient("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"))
                .thenThrow(new AlreadyExistException("patientId 1 existe déjà"));
        mockMvc.perform(post("/patient/validate")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "patientId 1 existe déjà"));
    }

    @Test
    public void postPatientValidateRunTimeException() throws Exception {
        when(patientManagementProxy.addPatient("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"))
                .thenThrow(new RuntimeException("patientId 1 existe déjà"));
        mockMvc.perform(post("/patient/validate")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème lors de la création du patient, réessayer plus tard"));
    }



    @Test
    public void postPatientValidateErreurFormulaire() throws Exception {
        when(patientManagementProxy.addPatient("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"))
                .thenReturn(existingPatient);
        mockMvc.perform(post("/patient/validate")
                .param("firstName","")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","G")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("patient", "sex", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("patient", "firstName", "NotBlank"))
                .andExpect(view().name("patient/add"));
    }
}