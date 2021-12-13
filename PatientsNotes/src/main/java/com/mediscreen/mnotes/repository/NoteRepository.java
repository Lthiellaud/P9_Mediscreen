package com.mediscreen.mnotes.repository;

import com.mediscreen.mnotes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Renvoi la liste des Notes enregistrées pour le patient
     * @param patientId Id su patient
     * @return La liste des notes
     */
    List<Note> findByPatientId(Integer patientId);
}
