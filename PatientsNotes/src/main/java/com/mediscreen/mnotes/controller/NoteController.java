package com.mediscreen.mnotes.controller;

import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api("API pour gérer les notes prises par les médecins")
@RestController
public class NoteController {
    
    @Autowired
    private NoteService noteService;

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

}
