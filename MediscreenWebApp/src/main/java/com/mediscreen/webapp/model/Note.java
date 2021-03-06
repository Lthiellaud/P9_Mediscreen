package com.mediscreen.webapp.model;

import com.mediscreen.webapp.validation.ValidNote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    private String id;

    private Integer patientId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noteDate;
    @ValidNote
    private String noteText;

    public Note(Integer patientId, String noteText) {
        this.noteText = noteText;
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

    public Note(Integer patientId) {
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

}
