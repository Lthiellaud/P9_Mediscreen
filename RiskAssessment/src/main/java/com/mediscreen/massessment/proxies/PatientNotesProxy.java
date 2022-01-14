package com.mediscreen.massessment.proxies;

import com.mediscreen.massessment.model.Triggers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "PatientsNotes", url = "${feign.url.note}")
public interface PatientNotesProxy {

    @PostMapping(value = "/patHistory/countNotesByPatientId/{id}")
    Long countNotesByPatientIdWithTrigger(@PathVariable Integer id,
                                           @RequestBody Triggers triggers);

}
