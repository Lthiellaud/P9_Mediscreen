package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.proxies.PatientManagementProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientController {

    @Autowired
    PatientManagementProxy patientManagementProxy;

    @GetMapping("/patient/list")
    public String listPatient(Model model) {
        model.addAttribute("patients", patientManagementProxy.getAllPatient());
        return "patient/list";
    }
}
