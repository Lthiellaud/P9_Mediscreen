package com.mediscreen.mpatients.controller;

import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.service.PatientService;
import com.mediscreen.mpatients.service.implementation.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping(value="/allPatients")
    public List<Patient> getAllPatient (){
        return new ArrayList<>();
    }
    
}
