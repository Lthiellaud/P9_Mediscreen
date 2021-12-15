package com.mediscreen.webapp.proxies;

import com.mediscreen.webapp.model.Note;
import com.mediscreen.webapp.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@FeignClient(name = "PatientsNotes", url = "${feign.url.note}")
public interface PatientNotesProxy {

    @GetMapping(value = "/patHistory/notesByPatientId/{id}")
    List<Note> getAllNotesByPatientId(@PathVariable Integer id);

    @GetMapping(value = "/patHistory/all")
    List<Note> getAllNotes();

    @PostMapping(value = "/patHistory/add")
    Note createNote(@RequestParam Integer patientId, @RequestParam("note") String note);
}
