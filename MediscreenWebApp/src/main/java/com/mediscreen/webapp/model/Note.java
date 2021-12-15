package com.mediscreen.webapp.model;

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
public class Note {

    private String id;

    private Integer patientId;
    private LocalDate noteDate;
    @NotBlank(message = "Merci de saisir votre note ou annuler")
    private String note;

    public Note(Integer patientId, String note) {
        this.note = note;
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

    public Note(Integer patientId) {
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

}
