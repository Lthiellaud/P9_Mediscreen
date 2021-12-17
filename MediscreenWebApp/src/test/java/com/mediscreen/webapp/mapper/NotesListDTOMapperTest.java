package com.mediscreen.webapp.mapper;

import com.mediscreen.webapp.model.DTO.NotesListDTO;
import com.mediscreen.webapp.model.Note;
import com.mediscreen.webapp.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotesListDTOMapperTest {

    @Test
    public void from() {
        Patient patient = new Patient(1, "Lucas","Ferguson"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");
        Note note = new Note("noteId1", 1,LocalDate.of(2021,12,1),"Nouvelle note");
        Note note2 = new Note("noteId2", 1,LocalDate.of(2021,12,1),"Nouvelle note 2");
        List<Note> notes = Arrays.asList(note, note2);

        NotesListDTO notesListDTO = NotesListDTOMapper.INSTANCE.from(patient, notes);
        assertThat(notesListDTO.getPatientId()).isEqualTo(1);
        assertThat(notesListDTO.getFirstName()).isEqualTo("Lucas");
        assertThat(notesListDTO.getLastName()).isEqualTo("Ferguson");
        assertThat(notesListDTO.getBirthDate()).isEqualTo(LocalDate.of(1980, 1, 1));
        assertThat(notesListDTO.getSex()).isEqualTo("M");
        assertThat(notesListDTO.getNotes().size()).isEqualTo(2);
        assertThat(notesListDTO.getNotes().get(1).getId()).isEqualTo("noteId2");
        assertThat(notesListDTO.getNotes().get(0).getNoteDate()).isEqualTo(LocalDate.of(2021,12,1));
        assertThat(notesListDTO.getNotes().get(0).getNoteText()).isEqualTo("Nouvelle note");
        assertThat(notesListDTO.getNotes().get(1).getNoteText()).isEqualTo("Nouvelle note 2");
    }
}
