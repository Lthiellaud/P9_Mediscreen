package com.mediscreen.webapp.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {

    private String id;

    private Integer patientId;
    private String firstName;
    private String lastName;
    private LocalDate noteDate;
    @NotBlank(message = "Texte à ajouter")
    private String note;

    public NoteDTO(Integer patientId, String note) {
        this.note = note;
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

}
