package com.mediscreen.mpatients.controller;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping(value = "/patient/list")
    public List<Patient> getAllPatient (){
        return patientService.getAllPatient();
    }

    /**
     * Pour obtenir les données du patient à partir de son id
     * @param id id patient
     * @return les données patient
     */
    @GetMapping(value = "/patient/patientById/{id}")
    public Patient getPatientById (@PathVariable Integer id){
        return patientService.getPatientById(id);
    }

    /**
     * Pour ajouter un patient
     * @param lastName Nom
     * @param firstName prénom
     * @param birthDate date de naissance
     * @param sex genre
     * @param address adresse postale
     * @param phone téléphone
     * @return le patient ajouté
     */
    @PostMapping(value = "/patient/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestParam("family") String lastName,
                                        @RequestParam("given") String firstName,
                                        @RequestParam ("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                                        @RequestParam String sex,
                                        @RequestParam String address,
                                        @RequestParam String phone) {

        return patientService.createPatient(lastName, firstName, birthDate, sex, address, phone);

    }

    /**
     * Pour mettre à jour un patient
     * @param patient le patient à mettre à jour
     * @return le patient mis à jour
     */
    @PutMapping(value = "/patient/update")
    public Patient updatePatient(@RequestBody Patient patient) {
        try {
            return patientService.updatePatient(patient);
        } catch (AlreadyExistException e) {
            System.out.println(e.getMessage());
            throw new AlreadyExistException(e.getMessage());
        }

    }

    /**
     * Pour supprimer un patient
     * @param id l'id du patient à supprimer
     */
    @DeleteMapping(value = "/patient/deletePatient/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
    }


}
