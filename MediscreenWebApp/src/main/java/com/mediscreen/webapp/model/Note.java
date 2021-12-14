package com.mediscreen.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    private String id;

    private Integer patientId;
    private LocalDate noteDate;
    private String note;

    public Note(Integer patientId, String note) {
        this.note = note;
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

}
