package com.mediscreen.webapp.mapper;

import com.mediscreen.webapp.model.DTO.AssessmentDTO;
import com.mediscreen.webapp.model.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AssessmentDTOMapperTest {

    @Test
    void from() {
        Patient patient = new Patient(1, "Lucas","Ferguson"
                , LocalDate.now().minusYears(30), "M", "2 Warren Street ", "387-866-1399");

        AssessmentDTO assessmentDTO = AssessmentDTOMapper.INSTANCE.from(patient, "None");
        assertThat(assessmentDTO.getFirstName()).isEqualTo("Lucas");
        assertThat(assessmentDTO.getLastName()).isEqualTo("Ferguson");
        assertThat(assessmentDTO.getBirthDate()).isEqualTo(LocalDate.now().minusYears(30));
        assertThat(assessmentDTO.getAge()).isEqualTo(30);
        assertThat(assessmentDTO.getSex()).isEqualTo("M");
        assertThat(assessmentDTO.getRiskAssessment()).isEqualTo("None");
    }
}