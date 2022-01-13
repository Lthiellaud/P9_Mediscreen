package com.mediscreen.mnotes.contoller;

import com.mediscreen.mnotes.controller.NoteController;
import com.mediscreen.mnotes.exception.NotFoundException;
import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.model.Triggers;
import com.mediscreen.mnotes.service.implementation.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    public void countNoteTest() throws Exception {
        RequestBuilder countRequest = MockMvcRequestBuilders
                .get("/patHistory/countNotesByPatientId/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"triggers\":[\"test\", \"trigger\"]}");
        mockMvc.perform(countRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0")))
                .andDo(print());
    }

    @Test
    public void addNoteTest() throws Exception {
        LocalDate aujourdHui = LocalDate.now();
        when(noteService.createNote(1,"Nouvelle note Patient 1"))
                .thenReturn(newNote);

        mockMvc.perform(post("/patHistory/add")
                    .param("patientId","1")
                    .param("note","Nouvelle note Patient 1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("noteDate", is(aujourdHui.toString())))
                .andExpect(jsonPath("noteText", is("Nouvelle note Patient 1")))
                .andDo(print());
    }

    @Test
    public void updateNoteTest() throws Exception {
        Note updatedNote = new Note("noteId", 1, LocalDate.of(2021, 12,1), "texte de la note");
        when(noteService.updateNote(any(Note.class))).thenReturn(updatedNote);
        RequestBuilder updateRequest = MockMvcRequestBuilders
                .put("/patHistory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"id\": \"noteId1\",\n" +
                        "    \"patientId\": \"1\",\n" +
                        "    \"noteDate\": \"2021-12-01\",\n" +
                        "    \"note\": \"texte de la note\"\n" +
                        "}");
        mockMvc.perform(updateRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("noteDate", is("2021-12-01")))
                .andExpect(jsonPath("noteText", is("texte de la note")))
                .andDo(print());
    }

    @Test
    public void updateNoteBadRequestTest() throws Exception {
        Note updatedNote = new Note("noteId", 1, LocalDate.of(2021, 12,1), "texte de la note");
        when(noteService.updateNote(any(Note.class))).thenReturn(updatedNote);
        RequestBuilder updateRequest = MockMvcRequestBuilders
                .put("/patHistory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"id\": \"noteId1\",\n" +
                        "    \"patientId\": \"1\",\n" +
                        "    \"noteDate\": \"2021-21-01\",\n" +
                        "    \"note\": \"Texte de la note\"\n" +
                        "}");
        mockMvc.perform(updateRequest)
                .andExpect(status().isBadRequest());

    }

    @Test
    public void updateNoteNotFoundTest() throws Exception {
        when(noteService.updateNote(any(Note.class))).thenThrow(new NotFoundException("non trouvé"));
        RequestBuilder createRequest = MockMvcRequestBuilders
                .put("/patHistory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"id\": \"noteId1\",\n" +
                        "    \"patientId\": \"1\",\n" +
                        "    \"noteDate\": \"2021-12-01\",\n" +
                        "    \"note\": \"Texte de la note\"\n" +
                        "}");
        mockMvc.perform(createRequest)
                .andExpect(status().isNotFound());

    }

}