package com.mediscreen.mpatients.service;

import com.mediscreen.mpatients.model.DTO.PatientDTO;
import com.mediscreen.mpatients.model.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getAllPatient();
    Patient getPatientById(Long id);
    Patient createPatient(PatientDTO patientDTO);
    Patient updatePatient(PatientDTO patientDTO, Long id);
    void deletePatient(Long id);
}
