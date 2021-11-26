package com.mediscreen.webapp.integration;

import com.mediscreen.webapp.model.Patient;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientManagementProxy patientManagementProxy;

    @Test
    void listPatientTest() throws Exception {
        mockMvc.perform(get("/patient/list"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updatePatientNotFound() throws Exception {

        mockMvc.perform(post("/patient/update/{id}", "0")
                .param("firstName","toto")
                .param("lastName","tutu")
                .param("birthDate","1980-12-01")
                .param("sex","M")
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().is(302))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "patientId 0 non trouvé"))
                .andExpect(redirectedUrl("/patient/list"))
                .andDo(print());

    }

    @Test
    void updatePatientAlreadyExist() throws Exception {

        Patient p = patientManagementProxy.getAllPatient().get(0);
        String i = String.valueOf(p.getPatientId() + 1);
        mockMvc.perform(post("/patient/update/{id}", i)
                .param("firstName",p.getFirstName())
                .param("lastName",p.getLastName())
                .param("birthDate",p.getBirthDate().toString())
                .param("sex",p.getSex())
                .param("homeAddress","2 Warren Street ")
                .param("phoneNumber","387-866-1399"))
                .andExpect(status().is(302))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Un patient existe déjà avec ces mêmes données clé (patientId "+
                        p.getPatientId() + ")"))
                .andExpect(redirectedUrl("/patient/list"))
                .andDo(print());

    }

}