package com.mediscreen.mnotes.contoller;

import com.mediscreen.mnotes.controller.NoteController;
import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.service.implementation.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoteController.class)
class NoteControllerTest {

    @MockBean
    private NoteServiceImpl noteService;

    @Autowired
    private MockMvc mockMvc;

    private static Note existingNote = new Note("idNote", 1, LocalDate.of(2021,12,1),"Texte de la note");
    private static Note newNote = new Note(1, "Nouvelle note Patient 1");

    @BeforeEach
    public void initTest() {
        existingNote.setId("IdNote");
    }

    @Test
    public void getAllNotesTest() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Arrays.asList(existingNote));
        mockMvc.perform(get("/patHistory/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().string(containsString("Texte de la note")))
                .andDo(print());
    }

    @Test
    public void getAllNotesByPatientTest() throws Exception {
        when(noteService.getAllNotesByPatientId(1)).thenReturn(Arrays.asList(existingNote));
        mockMvc.perform(get("/patHistory/notesByPatientId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().string(containsString("Texte de la note")))
                .andDo(print());
    }

    @Test
    public void addNoteTest() throws Exception {
        LocalDate aujourdHui = LocalDate.now();
        when(noteService.createNote(1,"Nouvelle note Patient 1"))
                .thenReturn(newNote);
        mockMvc.perform(post("/patHistory/add").param("patientId","1")
                .param("note","Nouvelle note Patient 1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("noteDate", is(aujourdHui.toString())))
                .andExpect(jsonPath("note", is("Nouvelle note Patient 1")))
                .andDo(print());
    }


}