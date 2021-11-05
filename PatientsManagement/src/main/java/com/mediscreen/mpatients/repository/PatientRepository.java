package com.mediscreen.mpatients.repository;


import com.mediscreen.mpatients.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findPatientByFirstNameAndLastNameAndBirthDateAndSex(String firstName, String lastName,
                                                                          LocalDate birthDate, String sex);
}
