package com.mediscreen.massessment.proxies;

import com.mediscreen.massessment.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "PatientsManagement", url = "${feign.url.patient}")
public interface PatientManagementProxy {

    @GetMapping(value = "/patient/patientById/{id}")
    Patient getPatientById(@PathVariable("id") Integer id);
}
