package com.mediscreen.mnotes.integration;

import com.mediscreen.mnotes.model.Note;
import com.mediscreen.mnotes.model.Triggers;
import com.mediscreen.mnotes.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NoteServiceIT {

    @Autowired
    public NoteService noteService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void resetCollection(){
        mongoTemplate.dropCollection(Note.class);
    }

    @Test
    public void countNoteByPatientIdWithTriggerTest() {
        noteService.createNote(1, "TesT nouvelle note1");
        noteService.createNote(1, "nouvelle note 2 TEST");
        noteService.createNote(1, "nouveau test note 3");
        noteService.createNote(1, "nouvelle note 4");
        noteService.createNote(11, "nouveau test note 5");
        noteService.createNote(1, "nouveau TriGGer note 6");
        noteService.createNote(1, "Test nouveau trigger test note 7");

        List<String> triggers1 = Arrays.asList("test");
        Long result = noteService.countNoteByPatientWithTrigger(1,new Triggers(triggers1));
        assertThat(result).isEqualTo(4);

        List<String> triggers2 = Arrays.asList("test", "trigger");
        Long result2 = noteService.countNoteByPatientWithTrigger(1,new Triggers(triggers2));
        assertThat(result2).isEqualTo(6);

        Long result3 = noteService.countNoteByPatientWithTrigger(2,new Triggers(triggers2));
        assertThat(result3).isEqualTo(0);

    }


}
