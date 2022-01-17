package com.mediscreen.mnotes.repository;

import com.mediscreen.mnotes.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomNoteRepositoryImpl implements CustomNoteRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Renvoi le nombre de note contenant un mot clé (ou plusieurs) pour un patient donné
     * @param patientId l'id du patient
     * @param trigger mot clé (regex)
     * @return le nombre de note
     */
    @Override
    public Long countNoteByPatientIdWithTrigger(Integer patientId, String trigger) {
        Query query = new Query();
        query.addCriteria(Criteria.where("patient_id").is(patientId));
        query.addCriteria(Criteria.where("note").regex(trigger, "i"));
        return mongoTemplate.count(query, Note.class);
    }
}
