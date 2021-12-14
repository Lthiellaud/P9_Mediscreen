package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.model.Note;
import com.mediscreen.webapp.model.Patient;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import com.mediscreen.webapp.proxies.PatientNotesProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoteController.class)
class NoteControllerTest {

    @MockBean
    private PatientNotesProxy patientNotesProxy;

    @MockBean
    private PatientManagementProxy patientManagementProxy;

    @Autowired
    private MockMvc mockMvc;

    private static final Patient existingPatient = new Patient(1, "Lucas","Ferguson"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");

    private static final Note existingNote = new Note("NoteId", 1, LocalDate.of(2021, 12, 25), "Texte de la note");

    @Test
    void listAllNotesTest() throws Exception {
        mockMvc.perform(get("/patHistory/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Aucune note enregistrée")))
                .andDo(print());
    }

    @Test
    void listNotesPatientTest() throws Exception {
        when(patientNotesProxy.getAllNotesByPatientId(1))
                .thenReturn(Arrays.asList(existingNote));
        when(patientManagementProxy.getPatientById(1)).thenReturn(existingPatient);
        mockMvc.perform(get("/patHistory/allByPatient/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("patient", existingPatient))
                .andExpect(model().attribute("notes", Arrays.asList(existingNote)))
                .andDo(print());
    }

    @Test
    void listNotesPatientNonTrouveTest() throws Exception {
        when(patientNotesProxy.getAllNotesByPatientId(1))
                .thenReturn(Arrays.asList(existingNote));
        when(patientManagementProxy.getPatientById(1)).thenThrow(new NotFoundException("Non trouvé"));
        mockMvc.perform(get("/patHistory/allByPatient/1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Non trouvé"));
    }

    @Test
    void listNotesPatientAutreExceptionTest() throws Exception {
        when(patientNotesProxy.getAllNotesByPatientId(1))
                .thenReturn(Arrays.asList(existingNote));
        when(patientManagementProxy.getPatientById(1)).thenThrow(new RuntimeException("Autre problème"));
        mockMvc.perform(get("/patHistory/allByPatient/1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard"));
    }


}