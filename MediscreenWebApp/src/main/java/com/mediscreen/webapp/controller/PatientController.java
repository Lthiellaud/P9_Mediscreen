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
    private PatientManagementProxy patientManagementProxy;


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
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
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
            attributes.addFlashAttribute("message", "Patient mis à jour");
        } catch (NotFoundException | AlreadyExistException e) {
            LOGGER.error("Problème pendant la mise à jour patient " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème pendant la mise à jour " + e.toString());
            attributes.addFlashAttribute("message", "Problème pendant la mise à jour, réessayer plus tard");
        }

        return "redirect:/patient/list";

    }

    @GetMapping("/patient/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute(new Patient());
        return "patient/add";

    }

    @PostMapping("/patient/validate")
    public String addPatient(@ModelAttribute("patient") @Valid Patient patient,
                                BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            LOGGER.debug("Mauvaise saisie");
            return "patient/add";
        }
        try {
            Patient addedPatient = patientManagementProxy.addPatient(
                    patient.getLastName(),
                    patient.getFirstName(),
                    patient.getBirthDate(),
                    patient.getSex(),
                    patient.getHomeAddress(),
                    patient.getPhoneNumber());
            LOGGER.info("Patient id " + addedPatient.getPatientId() + " créé ");
            model.addAttribute("message", "Patient id " + addedPatient.getPatientId() + " créé");
            return "patient/add";
        } catch (AlreadyExistException e) {
            LOGGER.error("Problème lors de la création du patient " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la création du patient " + e.toString());
            attributes.addFlashAttribute("message", "Problème lors de la création du patient, réessayer plus tard");
        }

        return "redirect:/patient/list";

    }


}
