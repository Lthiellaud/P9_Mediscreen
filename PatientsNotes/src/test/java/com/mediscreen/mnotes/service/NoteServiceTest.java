package com.mediscreen.mnotes.service;

import com.mediscreen.mnotes.exception.NotFoundException;
import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class NoteServiceTest {

    @MockBean
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @Test
    public void getAllNotesByPatientId() {
        List<Note> notesReponse = Arrays.asList(new Note(1,"Nouvelle note"));
        LocalDate aujourdHui = LocalDate.now();
        when(noteRepository.findByPatientId(1)).thenReturn(notesReponse);

        List<Note> notes =  noteService.getAllNotesByPatientId(1);

        assertThat(notes.size()).isEqualTo(1);
        assertThat(notes.get(0).getNoteText()).isEqualTo("Nouvelle note");
        assertThat(notes.get(0).getNoteDate()).isEqualTo(aujourdHui);
    }

    @Test
    public void getAllNotesByPatientIdNoNotes() {
        List<Note> notesReponse = new ArrayList<>();
        when(noteRepository.findByPatientId(1)).thenReturn(notesReponse);

        List<Note> notes =  noteService.getAllNotesByPatientId(1);

        assertThat(notes.size()).isEqualTo(0);
    }

    @Test
    public void getNoteById() {
        Note note = new Note("noteId1", 1,LocalDate.of(2021,12,1),"Nouvelle note");
        when(noteRepository.findById("noteId1")).thenReturn(Optional.of(note));

        Note note1 =  noteService.getNoteById("noteId1");

        assertThat(note1.getNoteDate()).isEqualTo(LocalDate.of(2021,12,1));
        assertThat(note1.getNoteText()).isEqualTo("Nouvelle note");
        assertThat(note1.getPatientId()).isEqualTo(1);
    }

    @Test
    public void getNoteByIdNotFound() {
        when(noteRepository.findById("noteId1")).thenThrow(new NotFoundException("note non trouvée"));

        Exception exception = assertThrows(NotFoundException.class, () -> noteService.getNoteById("noteId1"));

        assertThat(exception.getMessage()).isEqualTo("note non trouvée");
    }

    @Test
    public void updateNoteTest() {
        Note updatedNote = new Note("noteId", 1, LocalDate.of(2021, 12,1), "texte de la note");
        when(noteRepository.findById("noteId")).thenReturn(Optional.of(updatedNote));
        when(noteRepository.save(updatedNote)).thenReturn(updatedNote);

        Note note = noteService.updateNote(updatedNote);

        assertThat(note.getNoteText()).isEqualTo("texte de la note");
        assertThat(note.getNoteDate()).isEqualTo("2021-12-01");
        assertThat(note.getId()).isEqualTo("noteId");
    }

    @Test
    public void updateNoteNotFoundTest() {
        Note updatedNote = new Note("noteId", 1, LocalDate.of(2021, 12,1), "texte de la note");
        when(noteRepository.findById("noteId")).thenThrow(new NotFoundException("note non trouvée"));

        Exception exception = assertThrows(NotFoundException.class, () -> noteService.updateNote(updatedNote));

        assertThat(exception.getMessage()).isEqualTo("note non trouvée");
    }
}