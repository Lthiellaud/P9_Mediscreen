package com.mediscreen.webapp.proxies;

import com.mediscreen.webapp.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


@FeignClient(name = "PatientManagement", url = "${feign.url.patient}")
public interface PatientManagementProxy {

    @GetMapping(value="/patient/allPatients")
    List<Patient> getAllPatient();

    @PutMapping(value="/patient/update/{id}")
    Patient updatePatient(@PathVariable("id") Long id, Patient patient);

    @GetMapping(value = "/patient/patientById/{id}")
    Patient getPatientById(@PathVariable("id") Long id);
}
