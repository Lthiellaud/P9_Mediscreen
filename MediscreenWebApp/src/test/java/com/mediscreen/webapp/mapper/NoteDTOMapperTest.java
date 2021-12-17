package com.mediscreen.webapp.mapper;

import com.mediscreen.webapp.model.DTO.NoteDTO;
import com.mediscreen.webapp.model.Note;
import com.mediscreen.webapp.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class NoteDTOMapperTest {

    @Test
    public void from() {
        Patient patient = new Patient(1, "Lucas","Ferguson"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");
        Note note = new Note("noteId1", 1,LocalDate.of(2021,12,1),"Nouvelle note");

        NoteDTO noteDTO = NoteDTOMapper.INSTANCE.from(patient, note);
        assertThat(noteDTO.getId()).isEqualTo("noteId1");
        assertThat(noteDTO.getPatientId()).isEqualTo(1);
        assertThat(noteDTO.getFirstName()).isEqualTo("Lucas");
        assertThat(noteDTO.getLastName()).isEqualTo("Ferguson");
        assertThat(noteDTO.getBirthDate()).isEqualTo(LocalDate.of(1980, 1, 1));
        assertThat(noteDTO.getSex()).isEqualTo("M");
        assertThat(noteDTO.getNoteDate()).isEqualTo(LocalDate.of(2021,12,1));
        assertThat(noteDTO.getNoteText()).isEqualTo("Nouvelle note");
    }
}
