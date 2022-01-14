package com.mediscreen.webapp.mapper;

import com.mediscreen.webapp.model.DTO.AssessmentDTO;
import com.mediscreen.webapp.model.Patient;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Period;

@Mapper
public interface AssessmentDTOMapper {

    AssessmentDTOMapper INSTANCE = Mappers.getMapper(AssessmentDTOMapper.class);

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "patient.firstName", target = "firstName")
    @Mapping(source = "patient.lastName", target = "lastName")
    @Mapping(source = "patient.birthDate", target = "birthDate")
    @Mapping(target = "age", ignore = true)
    @Mapping(source = "patient.sex", target = "sex")
    @Mapping(source = "assessmentRisk", target = "assessmentRisk")
    AssessmentDTO from(Patient patient, Integer assessmentRisk);

    @AfterMapping
    default void calculateAge(Patient patient, @MappingTarget AssessmentDTO dto) {
        dto.setAge(Period.between(patient.getBirthDate(), LocalDate.now()).getYears());
    }
}
