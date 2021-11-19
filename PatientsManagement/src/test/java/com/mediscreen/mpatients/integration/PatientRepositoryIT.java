package com.mediscreen.mpatients.integration;

import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class PatientRepositoryIT {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void findTest() {
        Optional<Patient> patient = patientRepository.findPatientByFirstNameAndLastNameAndBirthDateAndSex("Lucas"
                , "Ferguson", LocalDate.of(1968, 6, 22), "M");

        assertThat(patient.isPresent()).isTrue();
        assertThat(patient.get().getPatientId()).isEqualTo(1);
    }

    @Test
    public void findByIdTest() {
        Optional<Patient> patient = patientRepository.findById(1);

        assertThat(patient.isPresent()).isTrue();
        assertThat(patient.get().getPatientId()).isEqualTo(1);
    }

    @Test
    public void findByLastNameTest() {
        List<Patient> patients = patientRepository.findByLastName("Ferguson");

        assertThat(patients.get(0).getPatientId()).isEqualTo(1);
        assertThat(patients.size()).isEqualTo(1);
    }
}
