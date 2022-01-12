package com.mediscreen.mnotes.service;

import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.model.Triggers;

import java.util.List;

public interface NoteService {

    List<Note> getAllNotesByPatientId(Integer patientId);
    List<Note> getAllNotes();
    Note createNote(Integer patientId, String note);
    Note updateNote(Note note);
    Note getNoteById(String id);
    Long countNoteByPatientWithTrigger(Integer patientId, Triggers triggers);
}
