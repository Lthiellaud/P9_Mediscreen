package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PatientController {

    @Autowired
    PatientManagementProxy patientManagementProxy;

    @GetMapping("/patient/list")
    public String listPatient(Model model) {
        model.addAttribute("patients", patientManagementProxy.getAllPatient());
        return "patient/list";
    }

    @GetMapping("/patient/update/{id}")
    public String showUpdatePatientForm(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("patient", patientManagementProxy.getPatientById(id));
            return "patient/update";
        } catch (NotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/patient/list";

    }


}
