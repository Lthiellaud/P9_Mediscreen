package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import com.mediscreen.webapp.proxies.PatientNotesProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    @Autowired
    private PatientNotesProxy patientNotesProxy;

    @Autowired
    private PatientManagementProxy patientManagementProxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    @GetMapping("/patHistory/allByPatient/{patientId}")
    public String listNotesByPatient(@PathVariable("patientId") Integer patientId, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("patient", patientManagementProxy.getPatientById(patientId));
            model.addAttribute("notes", patientNotesProxy.getAllNotesByPatientId(patientId));
            return "note/listPatient";
        } catch (NotFoundException e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard");
        }
        return "redirect:/patient/list";
    }

    @GetMapping("/patHistory/all")
    public String listNotes(Model model) {
        model.addAttribute("notes", patientNotesProxy.getAllNotes());
        return "note/list";
    }

}
