package com.mediscreen.mpatients.service;

import com.mediscreen.mpatients.model.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getAllPatient();
    Patient getPatientById(Long id);
    void createPatient(Patient patient);
    void updatePatient(Patient patient);
    void deletePatient(Long id);
}
