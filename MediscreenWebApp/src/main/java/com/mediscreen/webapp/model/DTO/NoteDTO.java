package com.mediscreen.webapp.model.DTO;

import com.mediscreen.webapp.validation.ValidNote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class NoteDTO {

    private String id;

    private Integer patientId;
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noteDate;
    @ValidNote
    private String noteText;

}
