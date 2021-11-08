package com.mediscreen.mpatients.controller;

import com.mediscreen.mpatients.model.DTO.PatientDTO;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping(value="/patient/allPatients")
    public List<Patient> getAllPatient (){
        return patientService.getAllPatient();
    }

    @PostMapping(value = "/patient/addPatient")
    public Patient addPatient(@RequestBody PatientDTO patientDTO) {
        return patientService.createPatient(patientDTO);
    }

    @PutMapping(value = "/patient/updatePatient")
    public Patient updatePatient(@RequestParam Long id, @RequestBody PatientDTO patientDTO) {
        return patientService.updatePatient(patientDTO, id);
    }

    @DeleteMapping(value = "/patient/deletePatient")
    public ResponseEntity<?> deletePatient(@RequestParam Long id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
