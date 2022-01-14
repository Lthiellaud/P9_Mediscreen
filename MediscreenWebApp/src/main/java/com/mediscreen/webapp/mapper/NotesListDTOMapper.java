package com.mediscreen.webapp.mapper;

import com.mediscreen.webapp.model.DTO.NotesListDTO;
import com.mediscreen.webapp.model.Note;
import com.mediscreen.webapp.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NotesListDTOMapper {

    NotesListDTOMapper INSTANCE = Mappers.getMapper(NotesListDTOMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "patient.firstName", target = "firstName")
    @Mapping(source = "patient.lastName", target = "lastName")
    @Mapping(source = "patient.birthDate", target = "birthDate")
    @Mapping(source = "patient.sex", target = "sex")
    @Mapping(source = "notes", target = "notes")
    NotesListDTO from(Patient patient, List<Note> notes);
}
