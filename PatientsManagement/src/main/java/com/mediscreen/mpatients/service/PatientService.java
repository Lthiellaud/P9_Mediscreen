package com.mediscreen.mpatients.service;

import com.mediscreen.mpatients.model.Patient;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {

    List<Patient> getAllPatient();
    Patient getPatientById(Integer id);
    List<Patient> getPatientByLastName(String lastName);
    Patient createPatient(String lastName, String firstName, LocalDate birthDate, String sex, String address, String phone);
    Patient updatePatient(Patient patient);
    void deletePatient(Integer id);

    Patient createPatient2(Patient patient);
}
