package com.mediscreen.mnotes.service.implementation;

import com.mediscreen.mnotes.exception.NotFoundException;
import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void updateNoteTest() {
        Note updatedNote = new Note("noteId", 1, LocalDate.of(2021, 12,1), "texte de la note");
        when(noteRepository.findById("noteId")).thenReturn(Optional.of(updatedNote));
        when(noteRepository.save(updatedNote)).thenReturn(updatedNote);

        Note note = noteService.updateNote(updatedNote);

        assertThat(note.getNote()).isEqualTo("texte de la note");
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