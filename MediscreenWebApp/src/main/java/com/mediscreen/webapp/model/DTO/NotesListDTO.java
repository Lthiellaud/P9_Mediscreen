package com.mediscreen.webapp.model.DTO;

import com.mediscreen.webapp.model.Note;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class NotesListDTO {

    private Integer patientId;
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String sex;
    private List<Note> notes;

}
