package com.mediscreen.mnotes.repository;

import com.mediscreen.mnotes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String>, CustomNoteRepository {

    /**
     * Renvoi la liste des Notes enregistrées pour le patient
     * @param patientId Id du patient
     * @return La liste des notes
     */
    List<Note> findByPatientId(Integer patientId);
}
