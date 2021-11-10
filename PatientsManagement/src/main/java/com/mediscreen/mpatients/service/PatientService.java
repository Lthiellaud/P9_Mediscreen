package com.mediscreen.mpatients.service;

import com.mediscreen.mpatients.model.DTO.PatientDTO;
import com.mediscreen.mpatients.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PatientService {

    List<Patient> getAllPatient();
    Patient getPatientById(Long id);
    List<Patient> getPatientByLastName(String lastName);
    Patient createPatient(String lastName, String firstName, LocalDate birthDate, String sex, String address, String phone);
    Patient createPatientDTO(PatientDTO patientDTO);
    Patient updatePatient(PatientDTO patientDTO, Long id);
    void deletePatient(Long id);
}
