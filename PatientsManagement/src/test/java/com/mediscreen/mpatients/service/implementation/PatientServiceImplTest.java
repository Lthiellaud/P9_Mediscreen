package com.mediscreen.mpatients.service.implementation;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.exception.NotFoundException;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PatientServiceImplTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private PatientServiceImpl patientService;

    private static Patient existingPatient = new Patient(1, "Lucas","Ferguson"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");

    private static Patient updatePatient = new Patient(2, "Lucas","Ferguson"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", null);

    @Test
    void createExistingPatient() {
        when(patientRepository.findPatientByFirstNameAndLastNameAndBirthDateAndSex("Lucas","Ferguson"
                , LocalDate.of(1980, 1, 1), "M")).thenReturn(Optional.of(existingPatient));
        Exception exception = assertThrows(AlreadyExistException.class, () -> patientService.createPatient("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399"));

        assertThat(exception.getMessage()).isEqualTo("Un patient existe déjà avec ces mêmes données clé (patientId 1)");
    }

    @Test
    void createPatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);
        Patient newPatient = patientService.createPatient("Ferguson", "Lucas"
                , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");

        assertThat(newPatient.getPatientId()).isEqualTo(1);
    }

    @Test
    void updatePatientCleModifieeEtUtilisee() {
        when(patientRepository.findById(2)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.findPatientByFirstNameAndLastNameAndBirthDateAndSex("Lucas", "Ferguson"
                , LocalDate.of(1980, 1, 1), "M")).thenReturn(Optional.of(existingPatient));

        Exception exception = assertThrows(AlreadyExistException.class, () -> patientService.updatePatient(updatePatient));
        assertThat(exception.getMessage()).isEqualTo("Un patient existe déjà avec ces mêmes données clé (patientId 1)");
    }

    @Test
    void updatePatient() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.findPatientByFirstNameAndLastNameAndBirthDateAndSex("Lucas", "Ferguson"
                , LocalDate.of(1980, 1, 1), "M")).thenReturn(Optional.of(existingPatient));

        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

        Patient patient = patientService.updatePatient(existingPatient);

        assertThat(patient.getPatientId()).isEqualTo(existingPatient.getPatientId());
    }

    @Test
    void getPatientById() {
        when(patientRepository.findById(1)).thenReturn(Optional.empty());

        Exception e = assertThrows(NotFoundException.class, () -> patientService.getPatientById(1));

        assertThat(e.getMessage()).isEqualTo("patientId 1 non trouvé");
    }

    @Test
    void getPatientByLastName() {
        when(patientRepository.findByLastName("toto")).thenReturn(new ArrayList<>());

        assertThat(patientService.getPatientByLastName("toto").size()).isEqualTo(0);
    }


}