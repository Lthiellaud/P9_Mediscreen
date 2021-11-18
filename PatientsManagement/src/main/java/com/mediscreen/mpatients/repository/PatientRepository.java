package com.mediscreen.mpatients.repository;


import com.mediscreen.mpatients.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    /**
     * Pour récupérer un patient à partir de la clé unique de la table patient
     * @param firstName prénom
     * @param lastName nom
     * @param birthDate date de naissance
     * @param sex sexe
     * @return Optional patient
     */
    Optional<Patient> findPatientByFirstNameAndLastNameAndBirthDateAndSex(String firstName, String lastName,
                                                                          LocalDate birthDate, String sex);

    /**
     * Pour récupérer un patient à partir de la clé unique de la table patient
     * @param lastName nom
    */
    List<Patient> findByLastName(String lastName);
}
