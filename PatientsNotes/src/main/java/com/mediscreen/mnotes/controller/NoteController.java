package com.mediscreen.mnotes.controller;

import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api("API pour gérer les notes prises par les médecins")
@RestController
public class NoteController {
    
    @Autowired
    private NoteService noteService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    @ApiOperation(value = "Liste des notes prises pour un patient")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK")})
    @GetMapping(value = "/patHistory/notesByPatientId/{id}")
    public List<Note> getAllNotesByPatientId(@PathVariable Integer id) {
        return noteService.getAllNotesByPatientId(id);
    }

    @ApiOperation(value = "Liste de l'ensemble des notes de la base")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK")})
    @GetMapping(value = "/patHistory/all")
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @ApiOperation(value = "Création d'une nouvelle note pour le patient {patId}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/patHistory/add")
    public Note createNote(@RequestParam("patientId") Integer patientId, @RequestParam("note") String note) {
        LOGGER.info("Note à ajouter pour le Patient id " + patientId);
        return noteService.createNote(patientId, note);
    }

}