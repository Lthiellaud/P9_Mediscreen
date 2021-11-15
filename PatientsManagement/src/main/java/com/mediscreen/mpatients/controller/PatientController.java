package com.mediscreen.mpatients.controller;

import com.mediscreen.mpatients.model.DTO.PatientDTO;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping(value="/patient/allPatients")
    public List<Patient> getAllPatient (){
        return patientService.getAllPatient();
    }

    @GetMapping(value="/patient/patientById")
    public Patient getPatientById (@RequestParam Long id){
        return patientService.getPatientById(id);
    }

    @GetMapping(value="/patient/patientByName")
    public List<Patient> getPatientById(@RequestParam String lastName){
        return patientService.getPatientByLastName(lastName);
    }

    @PostMapping(value = "/patient/addPatient")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatientDTO(@RequestBody PatientDTO patientDTO) {
        return patientService.createPatientDTO(patientDTO);
    }

    @PostMapping(value = "/patient/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestParam("family") String lastName,
                                        @RequestParam("given") String firstName,
                                        @RequestParam ("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                                        @RequestParam String sex,
                                        @RequestParam String address,
                                        @RequestParam String phone) {

        return patientService.createPatient(lastName, firstName, birthDate, sex, address, phone);

    }
    @PutMapping(value = "/patient/updatePatient")
    public Patient updatePatient(@RequestParam Long id, @RequestBody PatientDTO patientDTO) {
        return patientService.updatePatient(patientDTO, id);
    }

    @DeleteMapping(value = "/patient/deletePatient")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@RequestParam Long id) {
        patientService.deletePatient(id);
    }


}
