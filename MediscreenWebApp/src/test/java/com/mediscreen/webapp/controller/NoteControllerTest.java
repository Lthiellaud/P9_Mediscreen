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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString("<strong>Nom : </strong><span>Lucas Ferguson</span>")))
                .andExpect(content().string(containsString("<strong>Date de naissance : </strong><span>1980-01-01</span>")))
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
                .andExpect(flash().attribute("message", "Non trouvé"))
                .andExpect(flash().attribute("messageType", "error"))
                .andDo(print());
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
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard"));
    }

    @Test
    void showUpdateNoteForm() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenReturn(existingPatient);
        when(patientNotesProxy.getNoteById("NoteId")).thenReturn(existingNote);
        mockMvc.perform(get("/patHistory/update/1/NoteId"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString("<strong>Nom : </strong><span>Lucas Ferguson</span>")))
                .andExpect(content().string(containsString("<strong>Date de naissance : </strong><span>1980-01-01</span>")))
                .andExpect(content().string(containsString("Texte de la note")))
                .andDo(print());
    }

    @Test
    void showUpdateNoteFormNotFound() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenReturn(existingPatient);
        when(patientNotesProxy.getNoteById("NoteId")).thenThrow(new NotFoundException("Note non trouvée"));
        mockMvc.perform(get("/patHistory/update/1/NoteId"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Note non trouvée"));
    }

    @Test
    void showUpdateNoteFormAutreException() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenThrow(new RuntimeException("non trouvé"));
        when(patientNotesProxy.getNoteById("NoteId")).thenThrow(new NotFoundException("note non trouvée"));
        mockMvc.perform(get("/patHistory/update/1/NoteId"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard"));
    }

    @Test
    void updateNotePatient() throws Exception {
        when(patientNotesProxy.updateNote(any(Note.class))).thenReturn(new Note(1,"text note"));

        mockMvc.perform(post("/patHistory/update/1/noteId1")
                .param("id", "noteId1")
                .param("patientId", "1")
                .param("noteDate", "2021-12-01")
                .param("noteText", "text")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "success"))
                .andExpect(flash().attribute("message", "Note mise à jour pour le patient id 1"));
    }

    @Test
    void updateNotePatientTextVide() throws Exception {

        mockMvc.perform(post("/patHistory/update/1/noteId1")
                .param("id", "noteId1")
                .param("patientId", "1")
                .param("noteDate", "2021-12-01")
                .param("noteText", "<p><br><p>")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("noteDTO", "noteText", "ValidNote"))
                .andExpect(view().name("note/update"))
                .andDo(print());
    }

    @Test
    void updateNotePatientNonTrouve() throws Exception {
        when(patientNotesProxy.updateNote(any(Note.class))).thenThrow(new NotFoundException("Note non trouvée"));

        mockMvc.perform(post("/patHistory/update/1/noteId1")
                .param("id", "noteId1")
                .param("patientId", "1")
                .param("noteDate", "2021-12-01")
                .param("noteText", "text")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Note non trouvée"));
    }

    @Test
    void updateNotePatientAutreException() throws Exception {

        mockMvc.perform(post("/patHistory/update/1/noteId1")
                .param("id", "noteId1")
                .param("patientId", "1")
                .param("noteDate", "2021-12-01")
                .param("noteText", "text")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Problème lors de la mise à jour de la note, réessayer plus tard"));
    }

    @Test
    void showAddNoteForm() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenReturn(existingPatient);

        mockMvc.perform(get("/patHistory/add/1"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(content().string(containsString("<strong>Nom : </strong><span>Lucas Ferguson</span>")))
                .andExpect(content().string(containsString("<strong>Date de naissance : </strong><span>1980-01-01</span>")))
                .andExpect(content().string(containsString("Texte de la note à ajouter :")))
                .andDo(print());
    }

    @Test
    void showAddNoteFormNotFound() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenThrow(new NotFoundException("Note non trouvée"));

        mockMvc.perform(get("/patHistory/add/1"))
                .andExpect(status().is(302))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Note non trouvée"));
    }

    @Test
    void showAddNoteFormAutreException() throws Exception {
        when(patientManagementProxy.getPatientById(1)).thenThrow(new RuntimeException("Note non trouvée"));

        mockMvc.perform(get("/patHistory/add/1"))
                .andExpect(status().is(302))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard"));
    }

    @Test
    void addNotePatient() throws Exception {
        when(patientNotesProxy.createNote(1, "text note")).thenReturn(existingNote);

        mockMvc.perform(post("/patHistory/add/1")
                .param("id", "N")
                .param("patientId", "1")
                .param("noteDate", "2021-12-01")
                .param("noteText", "text note")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "success"))
                .andExpect(flash().attribute("message", "Note ajoutée pour le patient id 1"));
    }

    @Test
    void addNotePatientTextVide() throws Exception {

        mockMvc.perform(post("/patHistory/add/1")
                .param("id", "noteId")
                .param("patientId", "1")
                .param("noteDate", "2021-12-01")
                .param("noteText", "<p><br><p>")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("noteDTO", "noteText", "ValidNote"))
                .andExpect(view().name("note/add"))
                .andDo(print());
    }

    @Test
    void addNotePatientAutreException() throws Exception {

        mockMvc.perform(post("/patHistory/add/1")
                .param("id", "noteId1")
                .param("patientId", "1")
                .param("noteDate", "2021-12-01")
                .param("noteText", "text")
                .param("firstName","Lucas")
                .param("lastName","Ferguson")
                .param("birthDate","1980-01-01")
                .param("sex","M"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patHistory/allByPatient/1"))
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("messageType", "error"))
                .andExpect(flash().attribute("message", "Problème lors de la création de la note, réessayer plus tard"));
    }

}