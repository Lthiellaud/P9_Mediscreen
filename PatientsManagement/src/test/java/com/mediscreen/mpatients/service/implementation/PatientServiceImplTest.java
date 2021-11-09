package com.mediscreen.mpatients.service.implementation;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.model.DTO.PatientDTO;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class PatientServiceImplTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private PatientServiceImpl patientService;

    private static Patient existingPatient = new Patient(1L,"Ferguson", "Lucas"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399", null);

    private static PatientDTO patientDTO = new PatientDTO();

    private static PatientDTO updatedPatient = new PatientDTO();

    @BeforeEach
    public void initTest() {
        patientDTO.setFirstName("Ferguson");
        patientDTO.setLastName("Lucas");
        patientDTO.setBirthDate(LocalDate.of(1980, 1, 1));
        patientDTO.setSex("M");

        updatedPatient.setFirstName("Ferguson");
        updatedPatient.setLastName("Lucas");
        updatedPatient.setBirthDate(LocalDate.of(1980, 1, 1));
        updatedPatient.setSex("M");
    }

    @Test
    void createPatient() {
        when(patientRepository.findPatientByFirstNameAndLastNameAndBirthDateAndSex("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M")).thenReturn(Optional.of(existingPatient));
        Exception exception = assertThrows(AlreadyExistException.class, () -> patientService.createPatient(patientDTO));

        assertThat(exception.getMessage()).isEqualTo("Le patient Ferguson Lucas, né le 1980-01-01 - sexe M - existe déjà. Id : 1");
    }

    @Test
    void updatePatient() {
        when(patientRepository.findPatientByFirstNameAndLastNameAndBirthDateAndSex("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M")).thenReturn(Optional.of(existingPatient));

        Exception exception = assertThrows(AlreadyExistException.class, () -> patientService.updatePatient(updatedPatient, 2L));
        assertThat(exception.getMessage()).isEqualTo("Le patient Ferguson Lucas, né le 1980-01-01 - sexe M - existe déjà sous un id différent. Id à mettre à jour : 2"
                + " / id en base : 1");
    }

}