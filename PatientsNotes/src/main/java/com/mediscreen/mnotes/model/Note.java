package com.mediscreen.mnotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    @Field(value = "patient_id")
    private Integer patientId;
    @Field(value = "note_date")
    private LocalDate noteDate;
    @Field(value = "note")
    private String note;

    public Note(Integer patientId,String note) {
        this.note = note;
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

}
