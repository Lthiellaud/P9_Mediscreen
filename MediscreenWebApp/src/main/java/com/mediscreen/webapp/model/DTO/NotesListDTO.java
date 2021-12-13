package com.mediscreen.webapp.model.DTO;

import com.mediscreen.webapp.model.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesListDTO {

    private Integer patientId;
    private String firstName;
    private String lastName;
    private List<Note> notes;

}
