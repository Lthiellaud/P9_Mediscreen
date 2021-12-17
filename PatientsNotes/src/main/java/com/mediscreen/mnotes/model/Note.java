package com.mediscreen.mnotes.model;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "Id de la note", example="61b392404ca96f3df14bf7a4")
    @Id
    private String id;

    @ApiModelProperty(value = "Id du patient", required = true, example = "1", position = 1)
    @Field(value = "patient_id")
    private Integer patientId;
    @ApiModelProperty(value = "Date de création de la note", required = true, example = "2021-12-01", position = 2)
    @Field(value = "note_date")
    private LocalDate noteDate;
    @ApiModelProperty(value = "Note du médecin", required = true, example = "Note prise par le médecin", position = 3)
    @Field(value = "note")
    private String noteText;

    public Note(Integer patientId,String noteText) {
        this.noteText = noteText;
        this.patientId = patientId;
        this.noteDate = LocalDate.now();
    }

}
