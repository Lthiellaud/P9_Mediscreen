package com.mediscreen.mnotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Document("notes")
public class Note {

    @Id
    private String id;

    private Integer patientId;
    private LocalDate noteDate;
    private String note;
}
