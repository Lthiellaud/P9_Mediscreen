package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.mapper.NoteDTOMapper;
import com.mediscreen.webapp.mapper.NotesListDTOMapper;
import com.mediscreen.webapp.model.DTO.NoteDTO;
import com.mediscreen.webapp.model.Note;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import com.mediscreen.webapp.proxies.PatientNotesProxy;
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
public class NoteController {


    @Autowired
    private PatientNotesProxy patientNotesProxy;

    @Autowired
    private PatientManagementProxy patientManagementProxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    @GetMapping("/patHistory/allByPatient/{patientId}")
    public String listNotesByPatient(@PathVariable("patientId") Integer patientId, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("notesList", NotesListDTOMapper.INSTANCE.from(
                    patientManagementProxy.getPatientById(patientId), patientNotesProxy.getAllNotesByPatientId(patientId)));
            return "note/listPatient";
        } catch (NotFoundException e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard");
        }
        return "redirect:/patient/list";
    }

    @GetMapping("/patHistory/all")
    public String listNotes(Model model) {
        model.addAttribute("notes", patientNotesProxy.getAllNotes());
        return "note/list";
    }

    @GetMapping("/patHistory/add/{patientId}")
    public String showAddNoteForm(@PathVariable Integer patientId, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("noteDTO", NoteDTOMapper.INSTANCE.from(patientManagementProxy.getPatientById(patientId), new Note()));
            return "note/add";
        } catch (NotFoundException e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard");
        }
        return "redirect:/patHistory/allByPatient/" + patientId;
    }

    @PostMapping("/patHistory/add/{patientId}")
    public String addNote(@PathVariable Integer patientId, @ModelAttribute("noteDTO") @Valid NoteDTO noteDTO,
                          BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            LOGGER.debug("Mauvaise saisie");
            return "note/add";
        }
        try {
            Note addedNote = patientNotesProxy.createNote(patientId, noteDTO.getNoteText());
            LOGGER.info("Note id " + addedNote.getId() + " créée ");
            attributes.addFlashAttribute("messageType", "success");
            attributes.addFlashAttribute("message", "Note ajoutée pour le patient id " + addedNote.getPatientId());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la création de la note " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", "Problème lors de la création de la note, réessayer plus tard");
        }

        return "redirect:/patHistory/allByPatient/" + patientId;

    }

    @GetMapping("/patHistory/update/{patientId}/{noteId}")
    public String showUpdateNoteForm(@PathVariable Integer patientId, @PathVariable String noteId,
                                     Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("noteDTO", NoteDTOMapper.INSTANCE.from(
                    patientManagementProxy.getPatientById(patientId), patientNotesProxy.getNoteById(noteId)));
            return "note/update";
        } catch (NotFoundException e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard");
        }
        return "redirect:/patHistory/allByPatient/" + patientId;

    }

    @PostMapping("/patHistory/update/{patientId}/{noteId}")
    public String updateNote(@PathVariable Integer patientId, @PathVariable String noteId, @ModelAttribute("noteDTO") @Valid NoteDTO noteDTO,
                             BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            LOGGER.debug("Mauvaise saisie : " + result.getErrorCount() + " erreurs");
            return "note/update";
        }
        try {
            Note updatedNote = patientNotesProxy.updateNote(new Note(noteId, patientId, noteDTO.getNoteDate(), noteDTO.getNoteText()));
            LOGGER.info("Note id " + updatedNote.getId() + " mise à jour");
            attributes.addFlashAttribute("messageType", "success");
            attributes.addFlashAttribute("message", "Note mise à jour pour le patient id " + updatedNote.getPatientId());
        } catch (NotFoundException e) {
            LOGGER.error("Problème lors de la mise à jour de la note " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la mise à jour de la note " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", "Problème lors de la mise à jour de la note, réessayer plus tard");
        }

        return "redirect:/patHistory/allByPatient/" + patientId;

    }

}
