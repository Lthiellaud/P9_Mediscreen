package com.mediscreen.mpatients.controller;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.service.PatientService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Api("API pour gérer les patients")
@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "Liste des Patients suivis")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK")})
    @GetMapping(value = "/patient/list")
    public List<Patient> getAllPatient (){
        return patientService.getAllPatient();
    }

    /**
     * Pour obtenir les données du patient à partir de son id
     * @param id id patient
     * @return les données patient
     */
    @ApiOperation(value = "Récupérer les données d'un patient par son id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 404, message = "patientId {id} non trouvé") })
    @GetMapping(value = "/patient/patientById/{id}")
    public Patient getPatientById (@ApiParam(value = "id du patient") @PathVariable Integer id){
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
    @ApiOperation(value = "Ajouter un nouveau patient")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 409, message = "Un patient existe déjà avec ces mêmes données clé (patientId {id})")})
    @PostMapping(value = "/patient/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@ApiParam(value = "Nom de famille") @RequestParam("family") String lastName,
                              @ApiParam(value = "Prénom") @RequestParam("given") String firstName,
                              @ApiParam(value = "Date de naissance (format aaaa-mm-jj)") @RequestParam ("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                              @ApiParam(value = "Genre (F ou M)") @RequestParam String sex,
                              @ApiParam(value = "Adresse") @RequestParam(required = false) String address,
                              @ApiParam(value = "Téléphone") @RequestParam(required = false) String phone) {

        return patientService.createPatient(lastName, firstName, birthDate, sex, address, phone);

    }

    /**
     * Pour mettre à jour un patient
     * @param patient le patient à mettre à jour
     * @return le patient mis à jour
     */
    @ApiOperation(value = "Mettre à jour un  patient")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 404, message = "patientId {id} non trouvé"),
            @ApiResponse(code = 409, message = "Un patient existe déjà avec ces mêmes données clé (patientId {id})") })
    @PutMapping(value = "/patient/update")
    public Patient updatePatient(@RequestBody Patient patient) {
        return patientService.updatePatient(patient);

    }

    /**
     * Pour supprimer un patient
     * @param id l'id du patient à supprimer
     */
    @ApiOperation(value = "Supprimer un  patient")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "patientId {id} non trouvé")
    })
    @DeleteMapping(value = "/patient/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@ApiParam(value = "id du patient") @PathVariable Integer id) {
        patientService.deletePatient(id);
    }


}
