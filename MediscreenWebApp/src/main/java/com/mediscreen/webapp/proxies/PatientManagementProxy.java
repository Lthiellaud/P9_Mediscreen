package com.mediscreen.webapp.proxies;

import com.mediscreen.webapp.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "PatientManagement", url = "${feign.url.patient}")
public interface PatientManagementProxy {

    @GetMapping(value="/patient/allPatients")
    List<Patient> getAllPatient();

    
}
