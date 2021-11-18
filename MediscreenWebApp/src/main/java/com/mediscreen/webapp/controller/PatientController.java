package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.AlreadyExistException;
import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.model.Patient;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    PatientManagementProxy patientManagementProxy;


    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    @GetMapping("/patient/list")
    public String listPatient(Model model) {
        model.addAttribute("patients", patientManagementProxy.getAllPatient());
        return "patient/list";
    }

    @GetMapping("/patient/update/{id}")
    public String showUpdatePatientForm(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("patient", patientManagementProxy.getPatientById(id));
            return "patient/update";
        } catch (NotFoundException e) {
            LOGGER.error("Error during loading patient " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error during loading patient " + e.toString());
            attributes.addFlashAttribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard");
        }
        return "redirect:/patient/list";

    }

    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") Integer id, @ModelAttribute("patient") @Valid Patient patient,
                                BindingResult result, RedirectAttributes attributes) {
        patient.setPatientId(id);
        if (result.hasErrors()) {
            LOGGER.debug("Mauvaise saisie pour id : " + id);
            return "patient/update";
        }
        try {
            patientManagementProxy.updatePatient(patient);
            LOGGER.info("Patient id " + id + " updated");
            attributes.addFlashAttribute("message", "Update successful");
        } catch (NotFoundException | AlreadyExistException e) {
            LOGGER.error("Error during updating patient " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error during updating patient " + e.toString());
            attributes.addFlashAttribute("message", "Issue during updating, please retry later");
        }

        return "redirect:/patient/list";

    }


}
