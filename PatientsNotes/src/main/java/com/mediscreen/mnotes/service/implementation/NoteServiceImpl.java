package com.mediscreen.mnotes.service.implementation;

import com.mediscreen.mnotes.exception.NotFoundException;
import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.model.Triggers;
import com.mediscreen.mnotes.repository.NoteRepository;
import com.mediscreen.mnotes.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    private NoteRepository noteRepository;

    /**
     * Récupérer l'ensemble des notes pour un patient donné
     * @param patientId Id du patient
     * @return La liste des notes
     */
    @Override
    public List<Note> getAllNotesByPatientId(Integer patientId) {
        LOGGER.info("Demande des notes enregistrées pour patient " + patientId);
        return noteRepository.findByPatientId(patientId);
    }

    /**
     * Récupérer l'ensemble des notes
     * @return L'ensemble des notes enregistrées dans la base
     */
    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    /**
     * Sauvegarder une nouvelle note pour un patient donné.
     * @param patientId Id du patient
     * @param note la note à enregistrer
     * @return La note crée
     */
    @Override
    public Note createNote(Integer patientId, String note) {
        LOGGER.info("Demande d'ajout d'une note pour patient " + patientId);
        return noteRepository.save(new Note(patientId, note));
    }

    /**
     * Mise à jour d'une note existente.
     * @param note note
     * @return la note mise à jour
     */
    @Override
    public Note updateNote(Note note) {
        LOGGER.info("Demande de mise à jour d'une note pour patient " + note.getPatientId());
        getNoteById(note.getId());
        return noteRepository.save(note);
    }

    /**
     * Renvoi une note à partir de son id.
     * @param id à trouver
     * @return La note
     */
    @Override
    public Note getNoteById(String id) {
        return noteRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Note non trouvée");
            return new NotFoundException("noteId " + id + " non trouvé");
        });
    }

    /**
     * Renvoi le nombre de note contenant au moins un des mots clé d'une liste pour un patient donné.
     * @param patientId l'id du patient
     * @param triggers la liste de mot clé
     * @return le nombre de note
     */
    @Override
    public Long countNoteByPatientWithTrigger(Integer patientId, Triggers triggers) {
        String transformedTrigger = String.join("|", triggers.getTriggers()) ;
        return noteRepository.countNoteByPatientIdWithTrigger(patientId, transformedTrigger);
    }
}
