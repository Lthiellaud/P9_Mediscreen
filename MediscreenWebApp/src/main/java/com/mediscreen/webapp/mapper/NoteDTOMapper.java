package com.mediscreen.webapp.mapper;

import com.mediscreen.webapp.model.DTO.NoteDTO;
import com.mediscreen.webapp.model.Note;
import com.mediscreen.webapp.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoteDTOMapper {

    NoteDTOMapper INSTANCE = Mappers.getMapper(NoteDTOMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "patient.firstName", target = "firstName")
    @Mapping(source = "patient.lastName", target = "lastName")
    @Mapping(source = "patient.birthDate", target = "birthDate")
    @Mapping(source = "patient.sex", target = "sex")
    @Mapping(source = "note.id", target = "id")
    @Mapping(source = "note.noteDate", target = "noteDate")
    @Mapping(source = "note.noteText", target = "noteText")
    NoteDTO from(Patient patient, Note note);
}
