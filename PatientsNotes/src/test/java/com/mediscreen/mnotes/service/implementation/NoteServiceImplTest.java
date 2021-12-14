package com.mediscreen.mnotes.service.implementation;

import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class NoteServiceImplTest {

    @MockBean
    private NoteRepository noteRepository;

    @Autowired
    private NoteServiceImpl noteService;

    @Test
    public void getAllNotesByPatientId() {
        List<Note> notesReponse = Arrays.asList(new Note(1,"Nouvelle note"));
        LocalDate aujourdHui = LocalDate.now();
        when(noteRepository.findByPatientId(1)).thenReturn(notesReponse);
        List<Note> notes =  noteService.getAllNotesByPatientId(1);
        assertThat(notes.size()).isEqualTo(1);
        assertThat(notes.get(0).getNote()).isEqualTo("Nouvelle note");
        assertThat(notes.get(0).getNoteDate()).isEqualTo(aujourdHui);
    }
}