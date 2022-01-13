package com.mediscreen.mnotes.repository;

public interface CustomNoteRepository {
    Long countNoteByPatientIdWithTrigger(Integer patientId, String trigger);
}
