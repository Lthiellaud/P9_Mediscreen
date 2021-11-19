package com.mediscreen.webapp.proxies;

import com.mediscreen.webapp.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@FeignClient(name = "PatientManagement", url = "${feign.url.patient}")
public interface PatientManagementProxy {

    @GetMapping(value="/patient/list")
    List<Patient> getAllPatient();

    @PutMapping(value="/patient/update")
    Patient updatePatient(@RequestBody Patient patient);

    @PostMapping(value="/patient/add")
    Patient addPatient(@RequestParam("family") String lastName,
                       @RequestParam("given") String firstName,
                       @RequestParam ("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                       @RequestParam String sex,
                       @RequestParam String address,
                       @RequestParam String phone);

    @GetMapping(value = "/patient/patientById/{id}")
    Patient getPatientById(@PathVariable("id") Integer id);
}
