package com.mediscreen.mnotes.service;

import com.mediscreen.mnotes.model.Note;

import java.util.List;

public interface NoteService {

    List<Note> getAllNotesByPatientId(Integer patientId);
    List<Note> getAllNotes();
    Note createNote(Integer patientId, String note);
}
